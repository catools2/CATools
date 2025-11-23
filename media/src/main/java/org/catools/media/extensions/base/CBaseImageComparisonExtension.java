package org.catools.media.extensions.base;

import org.catools.common.extensions.base.CBaseObjectExtension;
import org.catools.media.extensions.states.interfaces.CImageComparisonState;

import java.awt.image.BufferedImage;

/**
 * CBaseBooleanExtension is a base interface for Boolean related extensions.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extended across all other objects:
 */
public interface CBaseImageComparisonExtension extends CBaseObjectExtension<BufferedImage, CImageComparisonState> {

  default CImageComparisonState _toState(BufferedImage e) {
    return () -> e;
  }
}