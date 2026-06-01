package com.nexusretail.common.utils;

import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.Browser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class UserAgentParserUtil {

    public UserAgentInfo parse(HttpServletRequest request) {
        if (request == null) return UserAgentInfo.empty();

        String uaString = request.getHeader("User-Agent");
        if (uaString == null || uaString.isBlank()) return UserAgentInfo.empty();

        UserAgent    ua      = UserAgent.parseUserAgentString(uaString);
        Browser      browser = ua.getBrowser();
        OperatingSystem os   = ua.getOperatingSystem();

        return UserAgentInfo.builder()
                .browserName(browser.getGroup().getName())
                .operatingSystem(os.getGroup().getName())
                .operatingSystemVersion(extractOsVersion(os, uaString))
                .deviceModel(os.getDeviceType().getName())
                .build();
    }

    private String extractOsVersion(OperatingSystem os, String uaString) {
        String fullName  = os.getName();
        String groupName = os.getGroup().getName();
        if (fullName.startsWith(groupName)) {
            String version = fullName.substring(groupName.length()).trim();
            return version.isEmpty() ? null : version;
        }
        return null;
    }

    @Getter
    @Builder
    public static class UserAgentInfo {
        private final String browserName;
        private final String operatingSystem;
        private final String operatingSystemVersion;
        private final String deviceModel;

        public static UserAgentInfo empty() {
            return UserAgentInfo.builder().build(); // all nulls
        }
    }
}