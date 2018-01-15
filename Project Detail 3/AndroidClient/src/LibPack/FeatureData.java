/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LibPack;

import java.io.Serializable;

public class FeatureData implements Serializable {

	private static final long serialVersionUID = 1L;
	public int TOTAL_OBJ;
	public int TOTAL_SAMPLE;
	public int TOTAL_FEATURE;
	public int TOTAL_FEATURE_COUNT;
	public double features_fast_color[][][][];
	public boolean featureInitialized[];

	public boolean annTrained;
	public double features_ANN[][];
	public double output_ANN[][];

	public String[] objectNames;

	public FeatureData() {
		TOTAL_OBJ = 3;
		TOTAL_SAMPLE = 1;
		TOTAL_FEATURE = 2;
		TOTAL_FEATURE_COUNT = 32;
		features_fast_color = new double[TOTAL_OBJ][TOTAL_SAMPLE][TOTAL_FEATURE][TOTAL_FEATURE_COUNT];
		featureInitialized = new boolean[] { false, false, false };

		annTrained = false;
		features_ANN = new double[TOTAL_OBJ * TOTAL_SAMPLE][TOTAL_FEATURE * TOTAL_FEATURE_COUNT];
		output_ANN = new double[TOTAL_OBJ * TOTAL_SAMPLE][TOTAL_OBJ];

		output_ANN = new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		objectNames = new String[3];

	}

	public void clearData() {
		for (int i = 0; i < TOTAL_OBJ; i++) {
			for (int j = 0; j < TOTAL_SAMPLE; j++) {
				for (int k = 0; k < TOTAL_FEATURE; k++) {
					for (int l = 0; l < TOTAL_FEATURE_COUNT; l++) {
						features_fast_color[i][j][k][l] = 0;
						features_ANN[i][k * l] = 0;
					}
				}
			}
		}
	}

}
