package com.quangtrung.HireAI.enums;

public enum City {
    HA_NOI("Hà Nội"),
    HUE("Thừa Thiên Huế"),
    HAI_PHONG("Hải Phòng"),
    DA_NANG("Đà Nẵng"),
    CAN_THO("Cần Thơ"),
    HO_CHI_MINH("Hồ Chí Minh"),
    CAO_BANG("Cao Bằng"),
    DIEN_BIEN("Điện Biên"),
    HA_TINH("Hà Tĩnh"),
    LAI_CHAU("Lai Châu"),
    LANG_SON("Lạng Sơn"),
    NGHE_AN("Nghệ An"),
    QUANG_NINH("Quảng Ninh"),
    THANH_HOA("Thanh Hóa"),
    SON_LA("Sơn La"),
    TUYEN_QUANG("Tuyên Quang"),
    LAO_CAI("Lào Cai"),
    THAI_NGUYEN("Thái Nguyên"),
    PHU_THO("Phú Thọ"),
    BAC_NINH("Bắc Ninh"),
    HUNG_YEN("Hưng Yên"),
    NINH_BINH("Ninh Bình"),
    QUANG_TRI("Quảng Trị"),
    QUANG_NGAI("Quảng Ngãi"),
    GIA_LAI("Gia Lai"),
    KHANH_HOA("Khánh Hòa"),
    LAM_DONG("Lâm Đồng"),
    DAK_LAK("Đắk Lắk"),
    DONG_NAI("Đồng Nai"),
    TAY_NINH("Tây Ninh"),
    VINH_LONG("Vĩnh Long"),
    DONG_THAP("Đồng Tháp"),
    CA_MAU("Cà Mau"),
    AN_GIANG("An Giang");

    private final String displayName;

    City(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}