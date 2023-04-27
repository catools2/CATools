package org.catools.media.model;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.date.CDate;
import org.catools.common.io.CFile;
import org.catools.media.enums.CImageComparisonType;
import org.catools.media.extensions.types.interfaces.CImageComparisonExtension;
import org.catools.media.utils.CImageComparisionUtil;
import org.catools.media.utils.CImageUtil;
import org.slf4j.Logger;

import java.awt.image.BufferedImage;

@Slf4j
public abstract class CScreenShot implements CImageComparisonExtension {

  @Override
  public boolean isEqual(BufferedImage expected) {
    return CImageComparisionUtil.getDiffs(get(), expected, CImageComparisonType.GRAY_FLOAT_32)
        .isEmpty();
  }

  @Override
  public int hashCode() {
    return get().hashCode();
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;

    if (!(obj instanceof BufferedImage))
      return false;

    return isEqual((BufferedImage) obj);
  }

  public CFile saveAsPng() {
    return saveAs("png", CFile.fromTmp(CDate.now().toTimeStampForFileName()));
  }

  public CFile saveAsPng(CFile file) {
    return saveAs("png", file);
  }

  public CFile saveAs(String formatName, CFile file) {
    if (get() == null) {
      return null;
    }
    CImageUtil.writeImage(get(), formatName, file);
    return file;
  }
}
