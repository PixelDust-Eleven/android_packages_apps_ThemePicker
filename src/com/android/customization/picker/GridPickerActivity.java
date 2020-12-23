/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.customization.picker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.customization.model.grid.GridOptionsManager;
import com.android.customization.model.grid.GridOption;
import com.android.customization.model.grid.LauncherGridOptionsProvider;
import com.android.customization.module.CustomizationInjector;
import com.android.customization.module.ThemesUserEventLogger;
import com.android.customization.picker.grid.GridFragment;
import com.android.customization.picker.grid.GridFragment.GridFragmentHost;
import com.android.wallpaper.R;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;

/**
 * Activity allowing for the grid picker to be linked to from other setup flows.
 *
 * This should be used with startActivityForResult. The resulting intent contains an extra
 * "grid_option" with the id of the picked grid option.
 */
public class GridPickerActivity extends FragmentActivity implements GridFragmentHost {

    private static final String EXTRA_GRID_OPTION = "grid_option";

    private GridOptionsManager mGridManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_option_picker);

        // Creating a class that overrides {@link GridManager#apply} to return the grid id to the
        // calling activity instead of putting the value into settings.
        //
        CustomizationInjector injector = (CustomizationInjector) InjectorProvider.getInjector();
        ThemesUserEventLogger eventLogger = (ThemesUserEventLogger) injector.getUserEventLogger(
                this);
        mGridManager = new GridOptionsManager(
                new LauncherGridOptionsProvider(this,
                        getString(R.string.grid_control_metadata_name)),
                eventLogger) {
        };
        if (!mGridManager.isAvailable()) {
            finish();
        } else {
            final FragmentManager fm = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fm.beginTransaction();
            final GridFragment gridFragment = GridFragment.newInstance(
                    getString(R.string.grid_title));
            fragmentTransaction.replace(R.id.fragment_container, gridFragment);
            fragmentTransaction.commitNow();
        }
    }

    @Override
    public GridOptionsManager getGridOptionsManager() {
        return mGridManager;
    }
}
