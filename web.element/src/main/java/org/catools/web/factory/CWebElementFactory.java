package org.catools.web.factory;

import org.catools.common.collections.CList;
import org.catools.common.utils.CStringUtil;
import org.catools.web.collections.CWebElements;
import org.catools.web.controls.CWebElement;
import org.catools.web.pages.CWebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

public class CWebElementFactory {
  /**
   * Similar to the other "initElements" methods, but takes an {@link FieldDecorator} which is used
   * for decorating each of the fields.
   *
   * @param page The CWebPage child to decorate the fields of
   */
  public static void initElements(CWebPage<?> page) {
    Class<?> proxyIn = page.getClass();
    while (proxyIn != Object.class) {
      decorateFields(page, proxyIn);
      proxyIn = proxyIn.getSuperclass();
    }
  }

  private static void decorateFields(CWebPage<?> page, Class<?> proxyIn) {
    Field[] fields = proxyIn.getDeclaredFields();
    for (Field field : fields) {
      Object value = decorateField(page, field);
      if (value != null) {
        try {
          field.setAccessible(true);
          field.set(page, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }


  private static Object decorateField(CWebPage<?> page, Field field) {
    if (!isDecoratable(field)) {
      return null;
    }

    assertValidAnnotations(field);

    if (CWebElement.class.isAssignableFrom(field.getType())) {
      return buildWebElement(page, field);
    } else if (CWebElements.class.isAssignableFrom(field.getType())) {
      return buildWebElements(page, field);
    } else {
      return null;
    }
  }

  private static boolean isDecoratable(Field field) {
    return CWebElement.class.isAssignableFrom(field.getType()) || CWebElements.class.isAssignableFrom(field.getType());
  }

  private static CWebElement<?> buildWebElement(CWebPage<?> page, Field field) {
    CFindBy elementInfo = field.getAnnotation(CFindBy.class);

    By by;
    if (elementInfo == null || elementInfo.findBy() == null) {
      by = new ByIdOrName(field.getName());
    } else {
      by = new FindBy.FindByBuilder().buildIt(elementInfo.findBy(), field);
    }

    String name = field.getName();
    if (elementInfo != null && CStringUtil.isNotBlank(elementInfo.name())) {
      name = elementInfo.name();
    }

    if (elementInfo == null || elementInfo.waitInSeconds() < 0)
      return new CWebElement<>(name, page.getDriver(), by);

    return new CWebElement<>(name, page.getDriver(), by, elementInfo.waitInSeconds());
  }

  private static CWebElements<?> buildWebElements(CWebPage<?> page, Field field) {
    CFindBys elementInfo = field.getAnnotation(CFindBys.class);
    if (CStringUtil.isBlank(elementInfo.xpath()))
      throw new IllegalArgumentException("CWebElements requires xpath (String) to find element.");

    String name = CStringUtil.defaultString(elementInfo.name(), field.getName());

    if (elementInfo.waitForFirstElementInSecond() < 0)
      return new CWebElements<>(name, page.getDriver(), elementInfo.xpath());

    if (elementInfo.waitForOtherElementInSecond() < 0)
      return new CWebElements<>(name, page.getDriver(), elementInfo.xpath(), elementInfo.waitForFirstElementInSecond());

    return new CWebElements<>(name, page.getDriver(), elementInfo.xpath(), elementInfo.waitForFirstElementInSecond(), elementInfo.waitForOtherElementInSecond());
  }

  private static void assertValidAnnotations(Field field) {
    CList<Annotation> seleniumAnnotatoins = CList.of(
        field.getAnnotation(FindBys.class),
        field.getAnnotation(FindAll.class),
        field.getAnnotation(FindBy.class));

    if (seleniumAnnotatoins.getAll(Objects::nonNull).isNotEmpty()) {
      throw new IllegalArgumentException("You should only use CFindBys or CFindBy annotation! (Attention to first Letter 'C')");
    }

    CFindBys cFindBys = field.getAnnotation(CFindBys.class);
    CList<Annotation> catoolsAnnotations = CList.of(cFindBys, field.getAnnotation(CFindBy.class));

    if (catoolsAnnotations.getAll(Objects::nonNull).size() > 1) {
      throw new IllegalArgumentException("You should use only one of CFindBys or CFindBy annotation!");
    }

    if (CWebElements.class.isAssignableFrom(field.getType()) && cFindBys == null) {
      throw new IllegalArgumentException("You should use CFindBys for CWebElements elements!");
    }

    if (CWebElement.class.isAssignableFrom(field.getType()) && cFindBys != null) {
      throw new IllegalArgumentException("You should use CFindBy for CWebElement elements!");
    }

    // Note that CWebElement default behaviour is to use by id or name, and it technically can exist without CFindBy
  }
}
