package org.catools.web.metrics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.catools.common.collections.CList;

@Data
@EqualsAndHashCode(callSuper = true)
public class CWebActionMetrics extends CList<CWebActionMetric> {
}
