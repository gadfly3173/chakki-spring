package vip.gadfly.chakkispring.vo;

import lombok.*;

/**
 * @author Gadfly
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokensWithMFA {
    private Boolean MFARequire;

    private String accessToken;

    private String refreshToken;
}
