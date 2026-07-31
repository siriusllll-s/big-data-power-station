package qrsoft.information.dto.output;

import org.junit.Assert;
import org.junit.Test;
import qrsoft.common.entity.Threshold;

/**
 * Drives real ThresholdOutput.entityToOutput (shipped mapping), not a reimplementation.
 */
public class ThresholdOutputTest {

	@Test
	public void entityToOutput_mapsAllFields() {
		Threshold e = new Threshold();
		e.setId(7);
		e.setClassification(2);
		e.setType(3);
		e.setLevel(1);
		e.setCycle(0);
		e.setStartTime(8);
		e.setEndTime(18);
		e.setIsEnable(0);
		e.setMemo("unit-test-memo");

		ThresholdOutput o = ThresholdOutput.entityToOutput(e);

		Assert.assertEquals(Integer.valueOf(7), o.getId());
		Assert.assertEquals(Integer.valueOf(2), o.getClassification());
		Assert.assertEquals(Integer.valueOf(3), o.getType());
		Assert.assertEquals(Integer.valueOf(1), o.getLevel());
		Assert.assertEquals(Integer.valueOf(0), o.getCycle());
		Assert.assertEquals(Integer.valueOf(8), o.getStartTime());
		Assert.assertEquals(Integer.valueOf(18), o.getEndTime());
		Assert.assertEquals(Integer.valueOf(0), o.getIsEnable());
		Assert.assertEquals("unit-test-memo", o.getMemo());
	}

	@Test
	public void entityToOutput_nullSafe() {
		ThresholdOutput o = ThresholdOutput.entityToOutput(null);
		Assert.assertNotNull(o);
		Assert.assertNull(o.getId());
	}
}
