package com.chikeandroid.debtmanager.features.persondetail;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.databinding.FragmentPersonDetailBinding;
import com.chikeandroid.debtmanager.features.debtdetail.DebtDetailActivity;
import com.chikeandroid.debtmanager.features.persondetail.adapter.PersonDebtsAdapter;
import com.chikeandroid.debtmanager.util.TimeUtil;
import com.chikeandroid.debtmanager.util.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/20/2017.
 * Person detail view
 */

public class PersonDetailFragment extends Fragment implements PersonDetailContract.View {

    public static final String EXTRA_PERSON = "com.chikeandroid.debtmanager20.features.debtdetail.DebtDetailFragment.extra_person";
    public static final String ARG_PERSON = "com.chikeandroid.debtmanager20.features.debtdetail.DebtDetailFragment.argument_person";

    private Person mPerson;
    private FragmentPersonDetailBinding mFragmentPersonDetailBinding;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private PersonDebtsAdapter mPersonDebtsAdapter;

    @Inject
    PersonDetailPresenter mPersonDetailPresenter;

    private PersonDetailContract.Presenter mPresenter;

    public static PersonDetailFragment newInstance(Person person) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PERSON, person);
        PersonDetailFragment personDetailFragment = new PersonDetailFragment();
        personDetailFragment.setArguments(args);
        return personDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPerson = getArguments().getParcelable(ARG_PERSON);
        }

        mPersonDebtsAdapter = new PersonDebtsAdapter(getActivity(), new ArrayList<>(0));

        DaggerPersonDetailComponent.builder()
                .personDetailPresenterModule(new PersonDetailPresenterModule(this, mPerson.getPhoneNumber()))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent()).build()
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFragmentPersonDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_person_detail, container, false);

        // FloatingActionButton fab = mFragmentDebtDetailBinding.fabScrolling;
        mCollapsingToolbarLayout = mFragmentPersonDetailBinding.collapsingToolbar;

        mCollapsingToolbarLayout.setTitle("");
        View view = mFragmentPersonDetailBinding.getRoot();

        RecyclerView recyclerView = mFragmentPersonDetailBinding.rvPersonDebts;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mPersonDebtsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mPersonDebtsAdapter.setOnItemClickListener((view1, debt, position) -> DebtDetailActivity.start(getActivity(), debt.getId(),
                debt.getDebtType()));

        setUpToolbar();

        return view;
    }

    private void setUpToolbar() {
        Toolbar toolbar = mFragmentPersonDetailBinding.toolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void setPresenter(PersonDetailContract.Presenter presenter) {
        checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showPersonDebts(List<Debt> debts) {

        mCollapsingToolbarLayout.setTitle(mPerson.getFullname());

        Glide.with(getActivity())
                .load(mPerson.getImageUri())
                .dontAnimate()
                .into(mFragmentPersonDetailBinding.image);

        Collections.sort(debts, (debt1, debt2) -> {

            Date personDebt1CreatedDate = TimeUtil.millis2Date(debt1.getCreatedDate());
            Date personDebt2CreatedDate = TimeUtil.millis2Date(debt2.getCreatedDate());

            return personDebt2CreatedDate.compareTo(personDebt1CreatedDate);
        });

        mPersonDebtsAdapter.updatePersonDebtListItems(debts);
    }

    @Override
    public void showMissingPersonDebts() {
        getActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
