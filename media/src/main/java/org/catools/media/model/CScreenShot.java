package org.catools.media.model;

import org.catools.common.date.CDate;
import org.catools.common.io.CFile;
import org.catools.media.enums.CImageComparisonType;
import org.catools.media.extensions.types.interfaces.CImageComparisionExtension;
import org.catools.media.utils.CImageComparisionUtil;
import org.catools.media.utils.CImageUtil;

import java.awt.image.BufferedImage;

public abstract class CScreenShot implements CImageComparisionExtension {

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
