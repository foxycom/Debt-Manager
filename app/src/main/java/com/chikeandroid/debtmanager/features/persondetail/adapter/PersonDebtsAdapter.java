package com.chikeandroid.debtmanager.features.persondetail.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.databinding.ListItemPersonDebtBinding;
import com.chikeandroid.debtmanager.features.persondetail.PersonDebtsDiffCallback;

import java.util.List;

/**
 * Created by Chike on 5/20/2017.
 * Person debts recyclerView adapter
 */

public class PersonDebtsAdapter extends RecyclerView.Adapter<PersonDebtsAdapter.ViewHolder> {

    private final List<Debt> mDebts;
    private final LayoutInflater mLayoutInflater;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;

    public PersonDebtsAdapter(Context context, List<Debt> debts) {
        mDebts = debts;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Debt debt, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, Debt debt, int position);
    }

   /* public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemPersonDebtBinding binding = ListItemPersonDebtBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Debt debt = mDebts.get(position);
        holder.bind(debt);
        holder.itemView.setOnLongClickListener(view -> {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemClick(view, debt, holder.getAdapterPosition());
            }
            return true;
        });

        holder.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, debt, holder.getAdapterPosition());
            }
        });

        if (System.currentTimeMillis() > debt.getDueDate()) {
            holder.mListItemPersonDebtBinding.tvDueDate.setTextColor(Color.RED);
        }
    }

    public void updatePersonDebtListItems(List<Debt> debts) {
        final PersonDebtsDiffCallback diffCallback = new PersonDebtsDiffCallback(this.mDebts, debts);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mDebts.clear();
        this.mDebts.addAll(debts);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mDebts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemPersonDebtBinding mListItemPersonDebtBinding;

        public ViewHolder(ListItemPersonDebtBinding binding) {
            super(binding.getRoot());
            mListItemPersonDebtBinding = binding;
        }

        public void bind(Debt debt) {
            mListItemPersonDebtBinding.setDebt(debt);
        }
    }
}
