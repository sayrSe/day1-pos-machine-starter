package pos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pos.machine.ItemsLoader.loadAllItems;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        Receipt receipt = calculateCost(receiptItems);

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

    private Receipt calculateCost(List<ReceiptItem> receiptItems) {
        receiptItems = calculateItemsCost(receiptItems);
        int totalPrice = calculateTotalPrice(receiptItems);

        return new Receipt(receiptItems, totalPrice);
    }

    private List<ReceiptItem> calculateItemsCost(List<ReceiptItem> receiptItems) {
        return receiptItems.stream()
                .peek(receiptItem -> receiptItem.setSubTotal(receiptItem.getUnitPrice() * receiptItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        return receiptItems.stream().mapToInt(ReceiptItem::getSubTotal).sum();
    }
}
