package com.chikeandroid.debtmanager.features.oweme;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.features.oweme.loader.OweMeLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 4/14/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link OweMePresenter}.
 */
@Module
public class OweMePresenterModule {

    private final OweMeContract.View mView;
    private final Fragment mContext;

    public OweMePresenterModule(OweMeContract.View view) {
        mView = view;
        mContext = (Fragment) view;
    }

    @Provides
    OweMeContract.View provideOweMeDebtsContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getLoaderManager();
    }

    @Provides
    OweMeLoader providesOweMeDebtsLoader(Context context, PersonDebtsRepository repository) {
        return new OweMeLoader(context, repository);
    }
}
