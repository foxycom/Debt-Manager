package com.chikeandroid.debtmanager.features.oweme.loader;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/15/2017.
 * Custom {@link android.content.Loader} for a list of {@link PersonDebt}, using the
 * {@link PersonDebtsRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class OweMeLoader extends AsyncTaskLoader<List<PersonDebt>> implements PersonDebtsRepository.DebtsRepositoryObserver {

    private final PersonDebtsRepository mDebtsRepository;

    public OweMeLoader(Context context, @NonNull PersonDebtsRepository repository) {
        super(context);
        checkNotNull(repository);
        mDebtsRepository = repository;
    }

    @Override
    public List<PersonDebt> loadInBackground() {

        // App is busy until further notice
        EspressoIdlingResource.increment();

        return mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
    }

    @Override
    public void deliverResult(List<PersonDebt> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded data immediately if available.
        if (mDebtsRepository.cachedOweMePersonDebtsAvailable()) {
            deliverResult(mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED));
        }

        // Begin monitoring the underlying data source
        mDebtsRepository.addContentObserver(this);

        if (takeContentChanged() || !mDebtsRepository.cachedOweMePersonDebtsAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    public void onDebtsChanged(int debtType) {
        if (debtType == Debt.DEBT_TYPE_OWED && isStarted()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mDebtsRepository.removeContentObserver(this);
    }
}
