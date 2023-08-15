package pos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pos.machine.ItemsLoader.loadAllItems;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        return null;
    }

    private List<ReceiptItem> decodeToItems(List<String> barcodes) {
        List<Item> items = loadAllItems();
        List<ReceiptItem> receiptItems = new ArrayList<>();
        List<String> distinctBarCodes = barcodes.stream().distinct().collect(Collectors.toList());

        for (String distinctBarcode : distinctBarCodes) {
            Item itemDetails = items.stream()
                    .filter(item -> item.getBarcode().equals(distinctBarcode))
                    .collect(Collectors.toList()).get(0);
            int count = (int) barcodes.stream().filter(barcode -> barcode.equals(distinctBarcode)).count();
            ReceiptItem receiptItem = new ReceiptItem(itemDetails.getName(), count, itemDetails.getPrice());
            receiptItems.add(receiptItem);
        }
        return receiptItems;
    }
}
