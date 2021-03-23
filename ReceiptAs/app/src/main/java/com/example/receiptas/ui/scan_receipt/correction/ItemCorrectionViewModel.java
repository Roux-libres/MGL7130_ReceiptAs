package com.example.receiptas.ui.scan_receipt.correction;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ItemCorrectionViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<CorrectableItem>> correctableItems = new MutableLiveData<>();
    private final ArrayList<Float> prices = new ArrayList<>();

    @Inject
    public ItemCorrectionViewModel() {

    }

    public void setCorrectableItemsFromList(ArrayList<String> items) {
        ArrayList<CorrectableItem> correctableItems = new ArrayList<>();
        for(String item : items) {
            correctableItems.add(new CorrectableItem(item));
        }
        this.correctableItems.setValue(correctableItems);
    }

    public MutableLiveData<ArrayList<CorrectableItem>> getCorrectableItems() {
        return this.correctableItems;
    }

    public ArrayList<String> getCorrectedItems() {
        ArrayList<String> correctedItems = new ArrayList<>();

        for(CorrectableItem item : this.correctableItems.getValue()) {
            if(!item.isDeleted()) {
                correctedItems.add(item.getLabel());
            } else {
                //do nothing
            }
        }
    return correctedItems;
    }

    public void setPricesFromParsedText(ArrayList<String> parsedText) throws Exception {
        DecimalFormat formatter = new DecimalFormat();
        DecimalFormatSymbols symbol = DecimalFormatSymbols.getInstance();
        symbol.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(symbol);

        String separatorFamily = ".,;:!?'\"";
        String regexSeparatorFamily = "[" + separatorFamily + "]";
        String regex = "[^0-9" + separatorFamily + "]";
        for(String text : parsedText) {
            text = text.replaceAll(regex, "");
            text = text.replaceAll(regexSeparatorFamily, String.valueOf(formatter.getDecimalFormatSymbols().getDecimalSeparator()));
            if(!TextUtils.isEmpty(text)) {
                this.prices.add(formatter.parse(text).floatValue());
            } else {
                //do nothing
            }
        }
    }

    public String getPreview(ArrayList<String> correctedItems, ArrayList<Float> prices, char currencySymbol, Context context) {
        StringBuilder builder = new StringBuilder();

        if (correctedItems.size() != prices.size()) {
            builder.append(context.getResources().getString(R.string.item_correction_dialog_quantity_caution));
        }
        int referenceSize = Math.max(correctedItems.size(), prices.size());

        builder.append(context.getResources().getString(R.string.item_correction_dialog_preview));
        for (int i = 0; i < referenceSize; i++) {
            builder
                .append("---------------\n")
                .append(i < correctedItems.size() ? correctedItems.get(i) : "no item")
                .append(" : ")
                .append(i < prices.size() ? prices.get(i) : "0")
                .append(currencySymbol)
                .append("\n");
        }

        return builder.toString();
    }

    public ArrayList<Float> getPrices() {
        return this.prices;
    }

    public class CorrectableItem {
        private String label;
        private boolean deleted;
        private CorrectableItem combinedItem;

        public CorrectableItem(String label) {
            this.label = label;
            this.deleted = false;
        }

        public String getLabel() {
            String completeLabel = this.label;
            if(this.combinedItem != null) {
                completeLabel = new StringBuilder(completeLabel)
                    .append(" ")
                    .append(combinedItem.getLabel()).toString();
            } else {
                //do nothing
            }
            return completeLabel;
        }

        public boolean isDeleted() {
            return this.deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public CorrectableItem getCombinedItem() {
            return this.combinedItem;
        }

        public CorrectableItem popCombinedItem() {
            CorrectableItem item = this.combinedItem;
            this.combinedItem = null;
            return item;
        }

        public CorrectableItem popLastCombinedItem() {
            CorrectableItem item = this.combinedItem;
            if(item != null) {
                if(this.combinedItem.getCombinedItem() != null) {
                    item = this.combinedItem.popLastCombinedItem();
                } else {
                    this.combinedItem = null;
                }
            } else {
                //do nothing
            }
            return item;
        }

        public void combineItem(CorrectableItem item) {
            if(this.combinedItem == null) {
                this.combinedItem = item;
            } else {
                this.combinedItem.combineItem(item);
            }
        }
    }
}
