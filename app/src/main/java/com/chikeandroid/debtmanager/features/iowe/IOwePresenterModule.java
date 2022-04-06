package com.chikeandroid.debtmanager.features.iowe;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.features.iowe.loader.IOweLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 5/1/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link IOwePresenter}.
 */
@Module
public class IOwePresenterModule {

    private final IOweContract.View mView;
    private final Fragment mContext;

    public IOwePresenterModule(IOweContract.View view) {
        mView = view;
        mContext = (Fragment) view;
    }

    @Provides
    IOweContract.View providesIOweContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getLoaderManager();
    }

    @Provides
    IOweLoader providesIOweLoader(Context context, PersonDebtsRepository repository) {
        return new IOweLoader(context, repository);
    }
}
