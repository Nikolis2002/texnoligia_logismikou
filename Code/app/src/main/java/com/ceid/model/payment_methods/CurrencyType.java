package com.ceid.model.payment_methods;

public enum CurrencyType
{
    DOLLAR("$", "Dollar"),
    EURO("€", "Euro");

    private final String symbol;
    private final String name;

    CurrencyType(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }
    CurrencyType(String symbol){
        this.symbol = symbol;
        this.name = getNameBySymbol(symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public static String getNameBySymbol(String symbol) {
        for (CurrencyType currency : CurrencyType.values()) {
            if (currency.getSymbol().equals(symbol)) {
                return currency.getName();
            }
        }
        return "Unknown";
    }

    public static String getSymbolByName(String name) {
        for (CurrencyType currency : CurrencyType.values()) {
            if (currency.getName().equals(name)) {
                return currency.getSymbol();
            }
        }
        return "Unknown";
    }


}