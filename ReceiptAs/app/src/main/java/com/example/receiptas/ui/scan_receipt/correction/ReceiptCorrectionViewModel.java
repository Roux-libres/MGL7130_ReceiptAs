package com.example.receiptas.ui.scan_receipt.correction;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReceiptCorrectionViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<CorrectableItem>> correctableItems = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> correctedItems = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> prices = new MutableLiveData<>();
    private final DecimalFormat formatter;

    @Inject
    public ReceiptCorrectionViewModel() {
        this.correctableItems.setValue(new ArrayList<>());
        this.correctedItems.setValue(new ArrayList<>());
        this.prices.setValue(new ArrayList<>());

       this.formatter = new DecimalFormat("#.##");
        DecimalFormatSymbols symbol = DecimalFormatSymbols.getInstance();
        symbol.setDecimalSeparator('.');
        this.formatter.setDecimalFormatSymbols(symbol);
    }

    public void setCorrectableItemsFromList(ArrayList<String> items, Context context) {
        ArrayList<CorrectableItem> correctableItems = new ArrayList<>();
        for(String item : items) {
            correctableItems.add(new CorrectableItem(item));
        }

        this.correctableItems.setValue(correctableItems);
        this.setCorrectedItems(correctableItems, this.prices.getValue(), context);
    }

    public void setPricesFromParsedText(ArrayList<String> parsedText, Context context) throws Exception {
        ArrayList<String> parsedPrices = new ArrayList();

        String separatorFamily = ".,;:!?'\"";
        String regexSeparatorFamily = "[" + separatorFamily + "]";
        String regex = "[^0-9" + separatorFamily + "]";
        for(String text : parsedText) {
            text = text.replaceAll(regex, "");
            text = text.replaceAll(regexSeparatorFamily, String.valueOf(this.formatter.getDecimalFormatSymbols().getDecimalSeparator()));
            if(!TextUtils.isEmpty(text)) {
                parsedPrices.add(text);
            } else {
                //do nothing
            }
        }

        this.setCorrectedItems(this.getCorrectableItems().getValue(), parsedPrices, context);
    }

    public float parseStringPrice(String price) throws ParseException {
        return this.formatter.parse(this.formatter.format(this.formatter.parse(price).floatValue())).floatValue();
    }

    public void setCorrectedItems(Context context) {
        this.setCorrectedItems(this.correctableItems.getValue(), this.prices.getValue(), context);
    }

    public void setCorrectedItems(
        ArrayList<CorrectableItem> correctableItems,
        ArrayList<String> prices,
        Context context
    ) {
        ArrayList<String> correctedItems = new ArrayList<>();

        for(CorrectableItem item : correctableItems) {
            if(!item.isDeleted()) {
                correctedItems.add(item.getLabel());
            } else {
                //do nothing
            }
        }

        this.balanceLists(correctedItems, prices, context);
    }

    public void balanceLists(ArrayList<String> correctedItems, ArrayList<String> prices, Context context) {
        int itemsSize = correctedItems.size();
        int pricesSize = prices.size();
        int referenceSize = Math.max(itemsSize, pricesSize);

        for (int i = 0; i < referenceSize - itemsSize; i++) {
            correctedItems.add(context.getResources().getString(R.string.item_placeholder));
        }

        for (int i = 0; i < referenceSize - pricesSize; i++) {
            prices.add(context.getResources().getString(R.string.price_placeholder));
        }


        for (int i = 0; i < correctedItems.size(); i++) {
            if(correctedItems.get(i).equals(context.getResources().getString(R.string.item_placeholder))
                && prices.get(i).equals(context.getResources().getString(R.string.price_placeholder))) {
                correctedItems.remove(i);
                prices.remove(i);
                i--;
            } else {
                //do nothing
            }
        }

        this.correctedItems.setValue(correctedItems);
        this.prices.setValue(prices);
    }

    public void changeLabel(int index, String newLabel) {
        this.correctedItems.getValue().set(index, newLabel);
        this.correctedItems.setValue(this.correctedItems.getValue());

        if(index < this.correctableItems.getValue().size()) {
            this.correctableItems.getValue().get(index).setLabel(newLabel);
            this.correctableItems.setValue(this.correctableItems.getValue());
        }
    }

    public void changePrice(int index, String newPrice) {
        this.prices.getValue().set(index, newPrice);
        this.prices.setValue(this.prices.getValue());
    }

    public LiveData<ArrayList<CorrectableItem>> getCorrectableItems() {
        return this.correctableItems;
    }

    public LiveData<ArrayList<String>> getPrices() {
        return this.prices;
    }

    public LiveData<ArrayList<String>> getCorrectedItems() {
        return this.correctedItems;
    }

    public DecimalFormat getFormatter() {
        return this.formatter;
    }

    public class CorrectableItem {
        public boolean get;
        private String label;
        private boolean deleted;
        private CorrectableItem combinedItem;
        private boolean hasBeenModifiedCombined;

        public CorrectableItem(String label) {
            this.label = label;
            this.deleted = false;
            this.hasBeenModifiedCombined = false;
        }

        public void setLabel(String label) {
            this.label = label;
            if(this.combinedItem != null) {
                this.hasBeenModifiedCombined = true;
            } else {
                //do nothing
            }
        }

        public String getLabel() {
            String completeLabel = this.label;
            if(this.combinedItem != null && !this.hasBeenModifiedCombined) {
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
            this.hasBeenModifiedCombined = false;
            return item;
        }

        public CorrectableItem popLastCombinedItem() {
            CorrectableItem item = this.combinedItem;
            if(item != null) {
                if(this.combinedItem.getCombinedItem() != null) {
                    item = this.combinedItem.popLastCombinedItem();
                } else {
                    this.combinedItem = null;
                    this.hasBeenModifiedCombined = false;
                }
            } else {
                this.hasBeenModifiedCombined = false;
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
