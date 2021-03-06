package com.example.receiptas.ui.history.receipt_detail;

import android.content.DialogInterface;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReceiptDetailFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MainViewModel mainViewModel;
    private String[] tabsNames;
    private int receiptId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        this.tabsNames = getResources().getStringArray(R.array.receipt_detail_tabs_names);
        this.receiptId = getArguments().getInt("receipt_id");

        NfcManager manager = (NfcManager) getContext().getSystemService(getContext().NFC_SERVICE);
        NfcAdapter nfcAdapter = manager.getDefaultAdapter();

        if(nfcAdapter != null) {
            this.setHasOptionsMenu(true);
        }

        if(((MainActivity) getActivity()).isTablet()) {
            Bundle arguments = new Bundle();
            arguments.putInt("receipt_id", this.receiptId);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.include_fragment_receipt_detail_products,
                            ReceiptDetailProductsFragment.class,
                            arguments)
                    .add(R.id.include_fragment_receipt_detail_summary,
                            ReceiptDetailSummaryFragment.class,
                            arguments)
                    .commit();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_detail, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share, menu);
        inflater.inflate(R.menu.delete, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((MainActivity) getActivity()).isTablet()) {
            TextView productsListHeader = view.findViewById(R.id.products_list_header);
            productsListHeader.setText(this.tabsNames[0]);

            TextView summaryHeader = view.findViewById(R.id.summary_header);
            summaryHeader.setText(this.tabsNames[1]);
        } else {
            tabLayout = view.findViewById(R.id.tab_layout);
            viewPager = view.findViewById(R.id.pager);

            tabLayout.addOnTabSelectedListener(tabSelectedListener);
            viewPager.setAdapter(new ReceiptDetailAdapter(this, tabLayout.getTabCount(), this.receiptId));

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabsNames[position])).attach();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_button){
            new MaterialAlertDialogBuilder(getContext())
                    .setMessage(getString(R.string.receipt_detail_deletion_message))
                    .setNeutralButton(R.string.receipt_detail_deletion_button_neutral, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    })
                    .setPositiveButton(R.string.receipt_detail_deletion_button_positive, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainViewModel.deleteReceipt(receiptId);
                            ((MainActivity) getActivity()).onSupportNavigateUp();
                        }
                    })
                    .show();
            return true;
        } else if(item.getItemId() == R.id.share_button) {
            ReceiptDetailFragmentDirections.ActionReceiptDetailFragmentToNavSendReceipt action =
                    ReceiptDetailFragmentDirections.actionReceiptDetailFragmentToNavSendReceipt(this.receiptId);
            Navigation.findNavController(getView()).navigate(action);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private final TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}