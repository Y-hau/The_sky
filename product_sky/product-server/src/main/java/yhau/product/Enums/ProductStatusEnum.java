package yhau.product.Enums;

public enum ProductStatusEnum {
    UP(0, "在架"),
    DOWN(1, "下架"),
    ;
    private Integer code;
    private String massage;

    ProductStatusEnum(Integer code, String massage) {
        this.code = code;
        this.massage = massage;
    }

    public Integer getCode() {
        return code;
    }

    public String getMassage() {
        return massage;
    }
}
