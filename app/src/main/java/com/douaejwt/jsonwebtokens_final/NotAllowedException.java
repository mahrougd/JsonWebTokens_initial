package com.douaejwt.jsonwebtokens_final;

import retrofit.RetrofitError;
/**
 * Created by douae on 12/11/17.
 */

public class NotAllowedException extends Throwable {
    public NotAllowedException(RetrofitError cause) {
        super(cause);
    }
}
