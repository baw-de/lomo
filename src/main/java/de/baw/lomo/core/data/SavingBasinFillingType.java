/*
 * Copyright (c) 2021-2024 Bundesanstalt für Wasserbau
 *
 * This file is part of LoMo.
 *
 * LoMo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * LoMo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoMo.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.baw.lomo.core.data;

import de.baw.lomo.utils.Utils;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="savingBasinFillingType")
public class SavingBasinFillingType extends AbstractSluiceGateFillingType {

	private double initialFillHeight = 6.0;

	private double floorHeight = 5.0;

	private List<KeyValueEntry> savingBasinSurfaceAreaLookup = new ArrayList<>();

	private double currentFillHeight = 0.0;
	private double lastTime = Double.MAX_VALUE;
	private double lastFlowRate = 0.0;

	public SavingBasinFillingType() {
		super();

		if (sluiceGateHeightLookup.isEmpty()) {
			sluiceGateHeightLookup.add(new KeyValueEntry(0.0, 0.0));
			sluiceGateHeightLookup.add(new KeyValueEntry(40.0, 0.04));
			sluiceGateHeightLookup.add(new KeyValueEntry(540.0, 1.0));
		}

		if (sluiceGateWidthLookup.isEmpty()) {
			sluiceGateWidthLookup.add(new KeyValueEntry(0., 8.0));
		}

		if (sluiceGateDischargeCoefficientLookup.isEmpty()) {
			sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0., 0.5));
		}

		if (jetOutletLookup.isEmpty()) {
			jetOutletLookup.add(new KeyValueEntry(0.0, 0.0));
			jetOutletLookup.add(new KeyValueEntry(1.0, 1.0 * 8.0 * 0.6));
		}

		if (savingBasinSurfaceAreaLookup.isEmpty()) {
			savingBasinSurfaceAreaLookup.add(new KeyValueEntry(0.0, 1000.0));
		}
	}

	public double getFloorHeight() {
		return floorHeight;
	}

	public void setFloorHeight(double floorHeight) {
		this.floorHeight = floorHeight;
	}

	public double getInitialFillHeight() {
		return initialFillHeight;
	}

	public void setInitialFillHeight(double initialFillHeight) {
		this.initialFillHeight = initialFillHeight;
	}

	@XmlElementWrapper
	@XmlElement(name = "entry") //$NON-NLS-1$
	public List<KeyValueEntry> getSavingBasinSurfaceAreaLookup() {
		return savingBasinSurfaceAreaLookup;
	}

	public void setSavingBasinSurfaceAreaLookup(List<KeyValueEntry> savingBasinSurfaceAreaLookup) {
		this.savingBasinSurfaceAreaLookup = savingBasinSurfaceAreaLookup;
	}

	public double getSavingBasinVolume(double fillHeight) {
		return Utils.linearIntegrate(savingBasinSurfaceAreaLookup,fillHeight);
	}

	public double getSavingBasinSurfaceArea(double fillHeight) {
		return Utils.linearInterpolate(savingBasinSurfaceAreaLookup,fillHeight);
	}

	public double getMaximumPressureHead() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getPressureHead(double time, double[] positions, double[] h, double[] v, Case data) {
		return Math.max(currentFillHeight - h[0], floorHeight - h[0]);
	}

	@Override
	public double[][] getSource(double time, double[] positions, double[] h, double[] v, Case data) {

		if (time < lastTime) {
			currentFillHeight = getInitialFillHeight();
		} else {
			currentFillHeight -= (time - lastTime) * lastFlowRate / getSavingBasinSurfaceArea(currentFillHeight);
		}

		final double[][] source = super.getSource(time, positions, h, v, data);

		lastFlowRate = source[0][0];
		lastTime = time;

		return source;
	}

	public double getBasinWaterLevel() {
		return currentFillHeight;
	}

	@Override
  	public String toString() {
    return Messages.getString("fillingTypeSavingBasin");
  }
}
