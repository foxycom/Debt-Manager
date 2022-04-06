package com.chikeandroid.debtmanager.features.iowe;

import androidx.annotation.NonNull;

import com.chikeandroid.debtmanager.base.BasePresenter;
import com.chikeandroid.debtmanager.base.BaseView;
import com.chikeandroid.debtmanager.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 * This specifies the contract between the view and the presenter.
 */

public interface IOweContract {

    interface View extends BaseView<Presenter> {

        void showDebts(List<PersonDebt> debts);

        void showLoadingDebtsError();

        void showEmptyView();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void batchDeletePersonDebts(@NonNull List<PersonDebt> personDebts, @NonNull int debtType);
    }
}
