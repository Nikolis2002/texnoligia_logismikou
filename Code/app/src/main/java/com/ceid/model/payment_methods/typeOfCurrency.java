public enum typeOfCurrency{
    DOLLAR("$", "Dollar"),
    EURO("€", "Euro");

    private final String symbol;
    private final String name;

    typeOfCurrency(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }
    typeOfCurrency(String symbol){
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
        for (typeOfCurrency currency : typeOfCurrency.values()) {
            if (currency.getSymbol().equals(symbol)) {
                return currency.getName();
            }
        }
        return "Unknown";
    }

    public static String getSymbolByName(String name) {
        for (typeOfCurrency currency : typeOfCurrency.values()) {
            if (currency.getName().equals(name)) {
                return currency.getSymbol();
            }
        }
        return "Unknown";
    }


}