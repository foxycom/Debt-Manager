package com.chikeandroid.debtmanager.features.oweme;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.databinding.OweMeFragmentBinding;
import com.chikeandroid.debtmanager.event.MainViewPagerSwipeEvent;
import com.chikeandroid.debtmanager.features.debtdetail.DebtDetailActivity;
import com.chikeandroid.debtmanager.features.oweme.adapter.OweMeAdapter;
import com.chikeandroid.debtmanager.util.StringUtil;
import com.chikeandroid.debtmanager.util.TimeUtil;
import com.chikeandroid.debtmanager.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/13/2017.
 * Display a List of {@link PersonDebt}s that people owe the user.
 */
public class OweMeFragment extends Fragment implements OweMeContract.View {

    private ActionMode mActionMode;
    private static final String TAG = "OweMeDebtsFragment";
    private OweMeAdapter mOweMeAdapter;
    private TextView mTextViewEmptyDebts;
    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private TextView mTextViewTotalAmount;

    @Inject
    OweMePresenter mOweMePresenter;

    private OweMeContract.Presenter mPresenter;

    public OweMeFragment() {
        // Required empty public constructor
    }

    public static OweMeFragment newInstance() {
        return new OweMeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mOweMeAdapter = new OweMeAdapter(this, new ArrayList<>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MainViewPagerSwipeEvent event) {
        if (event != null && mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        DaggerOweMeComponent.builder()
                .oweMePresenterModule(new OweMePresenterModule(this))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        OweMeFragmentBinding oweMeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.owe_me_fragment, container, false);
        final View view = oweMeFragmentBinding.getRoot();

        RecyclerView recyclerView = oweMeFragmentBinding.rvOweme;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mOweMeAdapter);
        mTextViewEmptyDebts = oweMeFragmentBinding.tvNoDebts;

        mTextViewTotalAmount = oweMeFragmentBinding.totalAmountLayout.tvTotalAmount;

        mOweMeAdapter.setOnItemClickListener((view1, personDebt, position) -> {
            if (mActionMode != null) {
                myToggleSelection(position, view1);
                return;
            }
            DebtDetailActivity.start(getActivity(), personDebt.getDebt().getId(),
                    personDebt.getDebt().getDebtType());
        });

        mOweMeAdapter.setOnItemLongClickListener((view12, personDebt, position) -> {
            mActionMode = getActivity().startActionMode(mActionModeCallback);
            myToggleSelection(position, view12);
        });

        return view;
    }

    @Override
    public void showDebts(List<PersonDebt> debts) {

        if (mTextViewEmptyDebts.getVisibility() == View.VISIBLE) {
            mTextViewEmptyDebts.setVisibility(View.GONE);
        }

        Collections.sort(debts, (personDebt1, personDebt2) -> {

            Date personDebt1CreatedDate = TimeUtil.millis2Date(personDebt1.getDebt().getCreatedDate());
            Date personDebt2CreatedDate = TimeUtil.millis2Date(personDebt2.getDebt().getCreatedDate());

            return personDebt2CreatedDate.compareTo(personDebt1CreatedDate);
        });

        mOweMeAdapter.updatePersonDebtListItems(debts);

        double total = 0;
        for (PersonDebt personDebt: debts) {
            total += personDebt.getDebt().getAmount();
        }

        mTextViewTotalAmount.setText(String.format(getString(R.string.total_debt_amount),
                StringUtil.commaNumber(total)));
    }

    @Override
    public void showEmptyView() {
        mOweMeAdapter.updatePersonDebtListItems(new ArrayList<>());
        mTextViewEmptyDebts.setVisibility(View.VISIBLE);
        mTextViewTotalAmount.setText(String.format(getString(R.string.total_debt_amount),
                StringUtil.commaNumber(0)));
    }

    @Override
    public void showLoadingDebtsError() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_loading_debts_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(OweMeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    private void myToggleSelection(int position, View view) {
        mOweMeAdapter.toggleSelection(position, view);
        String title = mOweMeAdapter.getSelectedItemCount() + " selected";
        mActionMode.setTitle(title);

    }

    // Define the callback when ActionMode is activated
    private class ActionModeCallback implements ActionMode.Callback {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.actions_debt, menu);
            //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            return true;
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == R.id.action_delete) {
                if (mOweMeAdapter.getSelectedItemCount() > 0) {
                    openConfirmDialog();

                }
                // mode.finish();
                return false;
            }
            return false;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode.finish();
            mActionMode = null;
            mOweMeAdapter.clearSelections();
        }
    }

    private void openConfirmDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(getString(R.string.dialog_delete), (dialogInterface, i) -> batchDelete())
                .setNegativeButton(getString(R.string.dialog_cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create();

        dialog.show();
    }

    public void batchDelete() {

        ProgressDialog progressDialog = ViewUtil.getProgressDialog(getActivity(), "Deleting...");
        progressDialog.show();

        int debtsDeleted = 0;

        List<PersonDebt> deletePersonDebts = new ArrayList<>();
        for (int i = 0; i < mOweMeAdapter.getSelectedItemCount(); i++) {
            int position = mOweMeAdapter.getSelectedItems().keyAt(i);
            PersonDebt personDebt = mOweMeAdapter.getPersonDebt(position);
            deletePersonDebts.add(personDebt);
            debtsDeleted++;
        }

        mPresenter.batchDeletePersonDebts(deletePersonDebts, Debt.DEBT_TYPE_OWED);

        if (debtsDeleted == mOweMeAdapter.getSelectedItemCount()) {
            ViewUtil.showToast(getActivity(), "Debts deleted successfully");
        }

        progressDialog.dismiss();
        mActionMode.finish();
    }
}
