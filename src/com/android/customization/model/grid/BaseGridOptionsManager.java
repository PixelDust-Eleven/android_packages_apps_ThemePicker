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
package com.android.customization.model.grid;

import com.android.customization.model.CustomizationManager;

/**
 * {@link CustomizationManager} for grid options.
 */
public abstract class BaseGridOptionsManager implements CustomizationManager<GridOption> {

    private final LauncherGridOptionsProvider mGridOptionsProvider;

    public BaseGridOptionsManager(LauncherGridOptionsProvider provider) {
        mGridOptionsProvider = provider;
    }

    @Override
    public boolean isAvailable() {
        return mGridOptionsProvider.areGridsAvailable();
    }

    @Override
    public void apply(GridOption option, Callback callback) {
        handleApply(option, callback);
    }

    @Override
    public void fetchOptions(OptionsFetchedListener<GridOption> callback, boolean reload) {
        mGridOptionsProvider.fetch(false);
    }

    /** Returns the ID of the current grid option, which may be null for the default grid option. */
    String getCurrentGrid() {
        return lookUpCurrentGrid();
    }

    /**
     * Implement to apply the grid picked by the user for {@link BaseGridManager#apply}.
     *
     * @param option Grid option, containing ID of the grid, that the user picked.
     * @param callback Report success and failure.
     */
    protected abstract void handleApply(GridOption option, Callback callback);

    /**
     * Implement to look up the current grid option for {@link BaseGridManager#getCurrentGrid()}.
     *
     * @return ID of current grid. Can be null for the default grid option.
     */
    protected abstract String lookUpCurrentGrid();
}
