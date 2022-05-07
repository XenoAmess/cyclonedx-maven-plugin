package org.cyclonedx.maven.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.spdx.library.InvalidSPDXAnalysisException;
import org.spdx.library.model.license.AnyLicenseInfo;
import org.spdx.library.model.license.ConjunctiveLicenseSet;
import org.spdx.library.model.license.DisjunctiveLicenseSet;

public class SpdxLicenseUtil {

    /**
     * Detect if a license pass black lists
     * @param license license
     * @param blackList license black list
     * @return if the license pass black lists
     * @throws InvalidSPDXAnalysisException actually shall never
     */
    public static boolean isLicensePassBlackList(
            AnyLicenseInfo license,
            String... blackList
    ) throws InvalidSPDXAnalysisException {
        if (license == null) {
            return true;
        }
        if (blackList == null || blackList.length == 0) {
            return true;
        }
        if (license instanceof ConjunctiveLicenseSet) {
            for (AnyLicenseInfo member : ((ConjunctiveLicenseSet) license).getMembers()) {
                if (!isLicensePassBlackList(member, blackList)) {
                    return false;
                }
            }
            return true;
        } else if (license instanceof DisjunctiveLicenseSet) {
            for (AnyLicenseInfo member : ((DisjunctiveLicenseSet) license).getMembers()) {
                if (isLicensePassBlackList(member, blackList)) {
                    return true;
                }
            }
            return false;
        } else {
            return !ArrayUtils.contains(blackList, license.toString());
        }
    }

    /**
     * Detect if a license pass white lists
     * @param license license
     * @param whiteList license white list
     * @return if the license pass white lists
     * @throws InvalidSPDXAnalysisException actually shall never
     */
    public static boolean isLicensePassWhiteList(
            AnyLicenseInfo license,
            String... whiteList
    ) throws InvalidSPDXAnalysisException {
        if (license == null) {
            return false;
        }
        if (whiteList == null || whiteList.length == 0) {
            return false;
        }
        if (license instanceof ConjunctiveLicenseSet) {
            for (AnyLicenseInfo member : ((ConjunctiveLicenseSet) license).getMembers()) {
                if (!isLicensePassWhiteList(member, whiteList)) {
                    return false;
                }
            }
            return true;
        } else if (license instanceof DisjunctiveLicenseSet) {
            for (AnyLicenseInfo member : ((DisjunctiveLicenseSet) license).getMembers()) {
                if (isLicensePassWhiteList(member, whiteList)) {
                    return true;
                }
            }
            return false;
        } else {
            return ArrayUtils.contains(whiteList, license.toString());
        }
    }

}
