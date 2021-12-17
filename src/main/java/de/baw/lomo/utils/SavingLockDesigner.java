/*
 * Copyright (c) 2021 Bundesanstalt für Wasserbau
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
package de.baw.lomo.utils;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.KeyValueEntry;
import de.baw.lomo.core.data.SavingBasinFillingType;
import de.baw.lomo.core.data.SluiceGateFillingType;

public class SavingLockDesigner {

    private final Case data;
    private double ratioAreaBasinToAreaChamber;
    private double nbBasins;
    private double restFillingHeightBasins;

    public SavingLockDesigner(Case data) {
        this.data = data;
    }

    public void setParameters(double nbBasins, double ratioAreaBasinToAreaChamber, double restFillingHeightBasins) {
        this.nbBasins = nbBasins;
        this.ratioAreaBasinToAreaChamber = ratioAreaBasinToAreaChamber;
        this.restFillingHeightBasins = restFillingHeightBasins;
    }

    public double getWaterDepthBasins() {
        final double dropHeight = Math.abs(data.getUpstreamWaterLevel() - data.getDownstreamWaterLevel());
        return (dropHeight - 2. * restFillingHeightBasins) / (ratioAreaBasinToAreaChamber * (nbBasins + 1.) + 1.);
    }

    public double getLamellaHeight() {
        return ratioAreaBasinToAreaChamber * getWaterDepthBasins();
    }

    public double getInitialFillingHeightBasins() {
        return getWaterDepthBasins() + getLamellaHeight() + restFillingHeightBasins;
    }

    public double getRestFillingHeightChamber() {
        return (1. + ratioAreaBasinToAreaChamber) * getWaterDepthBasins() + 2. * restFillingHeightBasins;
    }

    public double getLossOfWater() {
        return getRestFillingHeightChamber() * data.getChamberWidth() * data.getChamberLength();
    }

    public double getSavingRate() {
        final double dropHeight = Math.abs(data.getUpstreamWaterLevel() - data.getDownstreamWaterLevel());
        return nbBasins * getLamellaHeight() / dropHeight * 100;
    }

    public void generateSavingBasins() {
        // Delete all existing filling types
        data.getFillingTypes().clear();

        // Add saving basins
        double tFill = 0.0;

        for (int i = 1; i <= nbBasins; i++) {
            SavingBasinFillingType basin = new SavingBasinFillingType();
            basin.setName(String.format("%s %d", basin, i)); //$NON-NLS-1$
            basin.getSavingBasinSurfaceAreaLookup().get(0)
                    .setValue(ratioAreaBasinToAreaChamber * data.getChamberWidth() * data.getChamberLength());
            basin.setFloorHeight(Math.min(data.getDownstreamWaterLevel(), data.getUpstreamWaterLevel())
                    + i * getLamellaHeight() + restFillingHeightBasins);
            basin.setInitialFillHeight(basin.getFloorHeight() + getWaterDepthBasins());

            if (i == 1) {
                double[][] source;
                do {
                    source = basin.getSource(tFill, new double[]{0.0}, new double[]{basin.getFloorHeight()}, null, data);
                    tFill += 30.0;
                } while (tFill < 120. || source[0][0] > 0.1);
            } else {
                for (KeyValueEntry entry : basin.getSluiceGateHeightLookup()) {
                    entry.setKey(entry.getKey() + (i - 1) * tFill);
                }
            }

            basin.getSluiceGateHeightLookup().add(new KeyValueEntry(i * tFill - 1., basin.getSluiceGateHeightLookup().get(basin.getSluiceGateHeightLookup().size() - 1).getValue()));
            basin.getSluiceGateHeightLookup().add(new KeyValueEntry(i * tFill, 0.0));

            data.addFillingType(basin);
        }

        SluiceGateFillingType sluiceGate = new SluiceGateFillingType();
        for (KeyValueEntry entry : sluiceGate.getSluiceGateHeightLookup()) {
            entry.setKey(entry.getKey() + nbBasins * tFill);
        }

        data.addFillingType(sluiceGate);
    }
}
