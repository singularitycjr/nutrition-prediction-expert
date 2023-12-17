package com.tongji.common.constants;

import java.util.concurrent.TimeUnit;

public class CommonConstants {
    public static final String SMS_CODE = "sms_code:";
    public static final String SMS_UPDATE_CODE = "sms_update_code:";
    public static final Integer SMS_CODE_LENGTH = 6;
    public static final Integer SMS_CODE_TIMEOUT = 3;
    public static final TimeUnit SMS_CODE_TIMEOUT_TYPE = TimeUnit.MINUTES;

    public static final Integer SALT_LENGTH = 6;

    public static final String USER_IMG = "user_img:";
    public static final String USER_MASK_IMG = "user_mask_img:";
    public static final String FOOD_IMG = "food_img:";
    public static final Integer USER_IMG_TIMEOUT = 30;
    public static final TimeUnit USER_IMG_TIMEOUT_TYPE = TimeUnit.MINUTES;
}
