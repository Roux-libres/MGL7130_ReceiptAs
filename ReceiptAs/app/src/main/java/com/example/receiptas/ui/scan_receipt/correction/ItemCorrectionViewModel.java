package com.example.receiptas.ui.scan_receipt.correction;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.repository.MainRepository;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ItemCorrectionViewModel extends ViewModel {
    private MutableLiveData<ArrayList<CorrectableItem>> correctableItems = new MutableLiveData<>();

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

    public ArrayList<Float> getPricesFromParsedText(ArrayList<String> parsedText, Locale locale) throws Exception {
        ArrayList<Float> prices = new ArrayList<>();
        NumberFormat formatter = DecimalFormat.getInstance(locale);
        String regex = "[^0-9" + new DecimalFormatSymbols(locale).getDecimalSeparator() + "]";
        for(String text : parsedText) {
            text = text.replaceAll(regex, "");
            prices.add(formatter.parse(text).floatValue());
        }
        return prices;
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
