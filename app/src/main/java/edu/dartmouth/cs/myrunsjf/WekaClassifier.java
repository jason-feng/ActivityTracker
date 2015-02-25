package edu.dartmouth.cs.myrunsjf;

// Generated with Weka 3.6.12
//
// This code is public domain and comes with no warranty.
//
// Timestamp: Sat Feb 14 18:52:21 EST 2015

public class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N293f74c90(i);
        return p;
    }
    static double N293f74c90(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 118.673548) {
            p = WekaClassifier.N50c4d471(i);
        } else if (((Double) i[0]).doubleValue() > 118.673548) {
            p = WekaClassifier.N48b380617(i);
        }
        return p;
    }
    static double N50c4d471(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 3.659354) {
            p = WekaClassifier.N6aa572e72(i);
        } else if (((Double) i[6]).doubleValue() > 3.659354) {
            p = WekaClassifier.Na39a3df12(i);
        }
        return p;
    }
    static double N6aa572e72(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 21.627514) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 21.627514) {
            p = WekaClassifier.N141ad2643(i);
        }
        return p;
    }
    static double N141ad2643(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 0;
        } else if (((Double) i[12]).doubleValue() <= 0.899019) {
            p = WekaClassifier.N6ceaad8b4(i);
        } else if (((Double) i[12]).doubleValue() > 0.899019) {
            p = 0;
        }
        return p;
    }
    static double N6ceaad8b4(Object []i) {
        double p = Double.NaN;
        if (i[20] == null) {
            p = 0;
        } else if (((Double) i[20]).doubleValue() <= 0.691081) {
            p = WekaClassifier.N2327b88a5(i);
        } else if (((Double) i[20]).doubleValue() > 0.691081) {
            p = WekaClassifier.N3d1c422111(i);
        }
        return p;
    }
    static double N2327b88a5(Object []i) {
        double p = Double.NaN;
        if (i[31] == null) {
            p = 2;
        } else if (((Double) i[31]).doubleValue() <= 0.030694) {
            p = 2;
        } else if (((Double) i[31]).doubleValue() > 0.030694) {
            p = WekaClassifier.N4c47e0e6(i);
        }
        return p;
    }
    static double N4c47e0e6(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 3.524718) {
            p = WekaClassifier.N2fcb947(i);
        } else if (((Double) i[2]).doubleValue() > 3.524718) {
            p = 0;
        }
        return p;
    }
    static double N2fcb947(Object []i) {
        double p = Double.NaN;
        if (i[22] == null) {
            p = 2;
        } else if (((Double) i[22]).doubleValue() <= 0.089613) {
            p = 2;
        } else if (((Double) i[22]).doubleValue() > 0.089613) {
            p = WekaClassifier.N41de61a48(i);
        }
        return p;
    }
    static double N41de61a48(Object []i) {
        double p = Double.NaN;
        if (i[32] == null) {
            p = 2;
        } else if (((Double) i[32]).doubleValue() <= 0.020665) {
            p = 2;
        } else if (((Double) i[32]).doubleValue() > 0.020665) {
            p = WekaClassifier.N6edc77c49(i);
        }
        return p;
    }
    static double N6edc77c49(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 49.815626) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 49.815626) {
            p = WekaClassifier.N4c3b1db810(i);
        }
        return p;
    }
    static double N4c3b1db810(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 0;
        } else if (((Double) i[12]).doubleValue() <= 0.506941) {
            p = 0;
        } else if (((Double) i[12]).doubleValue() > 0.506941) {
            p = 2;
        }
        return p;
    }
    static double N3d1c422111(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 0;
        } else if (((Double) i[10]).doubleValue() <= 0.488445) {
            p = 0;
        } else if (((Double) i[10]).doubleValue() > 0.488445) {
            p = 2;
        }
        return p;
    }
    static double Na39a3df12(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (((Double) i[4]).doubleValue() <= 4.002928) {
            p = 0;
        } else if (((Double) i[4]).doubleValue() > 4.002928) {
            p = WekaClassifier.N4e358ab713(i);
        }
        return p;
    }
    static double N4e358ab713(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 2;
        } else if (((Double) i[9]).doubleValue() <= 6.746505) {
            p = WekaClassifier.N1d261c7e14(i);
        } else if (((Double) i[9]).doubleValue() > 6.746505) {
            p = 0;
        }
        return p;
    }
    static double N1d261c7e14(Object []i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 0;
        } else if (((Double) i[14]).doubleValue() <= 1.966821) {
            p = WekaClassifier.N3118a32515(i);
        } else if (((Double) i[14]).doubleValue() > 1.966821) {
            p = 2;
        }
        return p;
    }
    static double N3118a32515(Object []i) {
        double p = Double.NaN;
        if (i[32] == null) {
            p = 2;
        } else if (((Double) i[32]).doubleValue() <= 0.7382) {
            p = WekaClassifier.N487eeb5116(i);
        } else if (((Double) i[32]).doubleValue() > 0.7382) {
            p = 0;
        }
        return p;
    }
    static double N487eeb5116(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 3.893441) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() > 3.893441) {
            p = 2;
        }
        return p;
    }
    static double N48b380617(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 1370.550592) {
            p = WekaClassifier.N500b143e18(i);
        } else if (((Double) i[0]).doubleValue() > 1370.550592) {
            p = WekaClassifier.N3b3edaee85(i);
        }
        return p;
    }
    static double N500b143e18(Object []i) {
        double p = Double.NaN;
        if (i[23] == null) {
            p = 1;
        } else if (((Double) i[23]).doubleValue() <= 9.654417) {
            p = WekaClassifier.N75e1b8019(i);
        } else if (((Double) i[23]).doubleValue() > 9.654417) {
            p = WekaClassifier.N68bd698077(i);
        }
        return p;
    }
    static double N75e1b8019(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 429.311283) {
            p = WekaClassifier.N335f744720(i);
        } else if (((Double) i[0]).doubleValue() > 429.311283) {
            p = WekaClassifier.N77f6916257(i);
        }
        return p;
    }
    static double N335f744720(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 14.750796) {
            p = WekaClassifier.N3fcafbaa21(i);
        } else if (((Double) i[12]).doubleValue() > 14.750796) {
            p = WekaClassifier.N4e2753eb56(i);
        }
        return p;
    }
    static double N3fcafbaa21(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 251.018733) {
            p = WekaClassifier.N2750769e22(i);
        } else if (((Double) i[0]).doubleValue() > 251.018733) {
            p = WekaClassifier.N589ff0b643(i);
        }
        return p;
    }
    static double N2750769e22(Object []i) {
        double p = Double.NaN;
        if (i[32] == null) {
            p = 1;
        } else if (((Double) i[32]).doubleValue() <= 3.817268) {
            p = WekaClassifier.N139b933c23(i);
        } else if (((Double) i[32]).doubleValue() > 3.817268) {
            p = 0;
        }
        return p;
    }
    static double N139b933c23(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() <= 1.932397) {
            p = WekaClassifier.N4adf523224(i);
        } else if (((Double) i[21]).doubleValue() > 1.932397) {
            p = WekaClassifier.N1c25fdc138(i);
        }
        return p;
    }
    static double N4adf523224(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 2.237703) {
            p = WekaClassifier.Nb8d770525(i);
        } else if (((Double) i[9]).doubleValue() > 2.237703) {
            p = WekaClassifier.N5684ce5131(i);
        }
        return p;
    }
    static double Nb8d770525(Object []i) {
        double p = Double.NaN;
        if (i[31] == null) {
            p = 1;
        } else if (((Double) i[31]).doubleValue() <= 0.922681) {
            p = WekaClassifier.N7480ef2f26(i);
        } else if (((Double) i[31]).doubleValue() > 0.922681) {
            p = 0;
        }
        return p;
    }
    static double N7480ef2f26(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 0;
        } else if (((Double) i[10]).doubleValue() <= 0.39176) {
            p = 0;
        } else if (((Double) i[10]).doubleValue() > 0.39176) {
            p = WekaClassifier.N3cd7326a27(i);
        }
        return p;
    }
    static double N3cd7326a27(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() <= 0.388872) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() > 0.388872) {
            p = WekaClassifier.N542eec5a28(i);
        }
        return p;
    }
    static double N542eec5a28(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 4.003764) {
            p = WekaClassifier.N549f33e329(i);
        } else if (((Double) i[6]).doubleValue() > 4.003764) {
            p = 1;
        }
        return p;
    }
    static double N549f33e329(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 2;
        } else if (((Double) i[9]).doubleValue() <= 1.458974) {
            p = 2;
        } else if (((Double) i[9]).doubleValue() > 1.458974) {
            p = WekaClassifier.N2007a77c30(i);
        }
        return p;
    }
    static double N2007a77c30(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 3.182066) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 3.182066) {
            p = 2;
        }
        return p;
    }
    static double N5684ce5131(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() <= 1.515887) {
            p = WekaClassifier.N2905543732(i);
        } else if (((Double) i[21]).doubleValue() > 1.515887) {
            p = 1;
        }
        return p;
    }
    static double N2905543732(Object []i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 1;
        } else if (((Double) i[14]).doubleValue() <= 0.599357) {
            p = 1;
        } else if (((Double) i[14]).doubleValue() > 0.599357) {
            p = WekaClassifier.N1cdbfbeb33(i);
        }
        return p;
    }
    static double N1cdbfbeb33(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 0;
        } else if (((Double) i[21]).doubleValue() <= 0.492084) {
            p = 0;
        } else if (((Double) i[21]).doubleValue() > 0.492084) {
            p = WekaClassifier.N2e78101a34(i);
        }
        return p;
    }
    static double N2e78101a34(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 7.256755) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() > 7.256755) {
            p = WekaClassifier.N4c6932cb35(i);
        }
        return p;
    }
    static double N4c6932cb35(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 7.394967) {
            p = WekaClassifier.Ne85d49e36(i);
        } else if (((Double) i[6]).doubleValue() > 7.394967) {
            p = 1;
        }
        return p;
    }
    static double Ne85d49e36(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() <= 1.568128) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() > 1.568128) {
            p = WekaClassifier.N7449d88437(i);
        }
        return p;
    }
    static double N7449d88437(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 31.82444) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() > 31.82444) {
            p = 1;
        }
        return p;
    }
    static double N1c25fdc138(Object []i) {
        double p = Double.NaN;
        if (i[31] == null) {
            p = 2;
        } else if (((Double) i[31]).doubleValue() <= 1.773152) {
            p = WekaClassifier.N236985739(i);
        } else if (((Double) i[31]).doubleValue() > 1.773152) {
            p = WekaClassifier.N3ceda73d41(i);
        }
        return p;
    }
    static double N236985739(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 205.874504) {
            p = WekaClassifier.N4e4b78e340(i);
        } else if (((Double) i[0]).doubleValue() > 205.874504) {
            p = 0;
        }
        return p;
    }
    static double N4e4b78e340(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 0;
        } else if (((Double) i[7]).doubleValue() <= 2.014791) {
            p = 0;
        } else if (((Double) i[7]).doubleValue() > 2.014791) {
            p = 2;
        }
        return p;
    }
    static double N3ceda73d41(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 23.718815) {
            p = WekaClassifier.N167eb50b42(i);
        } else if (((Double) i[3]).doubleValue() > 23.718815) {
            p = 1;
        }
        return p;
    }
    static double N167eb50b42(Object []i) {
        double p = Double.NaN;
        if (i[20] == null) {
            p = 1;
        } else if (((Double) i[20]).doubleValue() <= 2.275813) {
            p = 1;
        } else if (((Double) i[20]).doubleValue() > 2.275813) {
            p = 0;
        }
        return p;
    }
    static double N589ff0b643(Object []i) {
        double p = Double.NaN;
        if (i[17] == null) {
            p = 1;
        } else if (((Double) i[17]).doubleValue() <= 6.010306) {
            p = WekaClassifier.N6c746a2e44(i);
        } else if (((Double) i[17]).doubleValue() > 6.010306) {
            p = WekaClassifier.N3d83db5a54(i);
        }
        return p;
    }
    static double N6c746a2e44(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 10.218817) {
            p = WekaClassifier.N4ee729a245(i);
        } else if (((Double) i[64]).doubleValue() > 10.218817) {
            p = 1;
        }
        return p;
    }
    static double N4ee729a245(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 13.818966) {
            p = WekaClassifier.N2a5e732646(i);
        } else if (((Double) i[4]).doubleValue() > 13.818966) {
            p = WekaClassifier.N7057ba9548(i);
        }
        return p;
    }
    static double N2a5e732646(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 9.975573) {
            p = WekaClassifier.N1ed9d98547(i);
        } else if (((Double) i[64]).doubleValue() > 9.975573) {
            p = 0;
        }
        return p;
    }
    static double N1ed9d98547(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 57.830601) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 57.830601) {
            p = 0;
        }
        return p;
    }
    static double N7057ba9548(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 2.086846) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() > 2.086846) {
            p = WekaClassifier.Nf9ac5d249(i);
        }
        return p;
    }
    static double Nf9ac5d249(Object []i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 0;
        } else if (((Double) i[14]).doubleValue() <= 1.676711) {
            p = 0;
        } else if (((Double) i[14]).doubleValue() > 1.676711) {
            p = WekaClassifier.N7a3165fe50(i);
        }
        return p;
    }
    static double N7a3165fe50(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 50.242229) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 50.242229) {
            p = WekaClassifier.N411f455e51(i);
        }
        return p;
    }
    static double N411f455e51(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 31.738896) {
            p = WekaClassifier.N6c033fb852(i);
        } else if (((Double) i[2]).doubleValue() > 31.738896) {
            p = WekaClassifier.N494c806e53(i);
        }
        return p;
    }
    static double N6c033fb852(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 2;
        } else if (((Double) i[11]).doubleValue() <= 3.966001) {
            p = 2;
        } else if (((Double) i[11]).doubleValue() > 3.966001) {
            p = 0;
        }
        return p;
    }
    static double N494c806e53(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 59.404039) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 59.404039) {
            p = 0;
        }
        return p;
    }
    static double N3d83db5a54(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (((Double) i[4]).doubleValue() <= 17.809895) {
            p = 2;
        } else if (((Double) i[4]).doubleValue() > 17.809895) {
            p = WekaClassifier.N1bb6114355(i);
        }
        return p;
    }
    static double N1bb6114355(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 24.613635) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 24.613635) {
            p = 1;
        }
        return p;
    }
    static double N4e2753eb56(Object []i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() <= 8.472866) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() > 8.472866) {
            p = 0;
        }
        return p;
    }
    static double N77f6916257(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 79.434749) {
            p = WekaClassifier.N5cc2f67558(i);
        } else if (((Double) i[2]).doubleValue() > 79.434749) {
            p = WekaClassifier.N7d84a5d672(i);
        }
        return p;
    }
    static double N5cc2f67558(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 10.085235) {
            p = WekaClassifier.N3c2aae759(i);
        } else if (((Double) i[12]).doubleValue() > 10.085235) {
            p = WekaClassifier.N67bc9eb766(i);
        }
        return p;
    }
    static double N3c2aae759(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 25.07057) {
            p = WekaClassifier.N6162259e60(i);
        } else if (((Double) i[5]).doubleValue() > 25.07057) {
            p = WekaClassifier.N253dcb0264(i);
        }
        return p;
    }
    static double N6162259e60(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 9.348148) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 9.348148) {
            p = WekaClassifier.N7293da0461(i);
        }
        return p;
    }
    static double N7293da0461(Object []i) {
        double p = Double.NaN;
        if (i[25] == null) {
            p = 1;
        } else if (((Double) i[25]).doubleValue() <= 1.660936) {
            p = WekaClassifier.N48ce776062(i);
        } else if (((Double) i[25]).doubleValue() > 1.660936) {
            p = 1;
        }
        return p;
    }
    static double N48ce776062(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 4.224621) {
            p = WekaClassifier.N6aff64f763(i);
        } else if (((Double) i[11]).doubleValue() > 4.224621) {
            p = 2;
        }
        return p;
    }
    static double N6aff64f763(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 4.719814) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() > 4.719814) {
            p = 0;
        }
        return p;
    }
    static double N253dcb0264(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (((Double) i[4]).doubleValue() <= 29.812259) {
            p = 2;
        } else if (((Double) i[4]).doubleValue() > 29.812259) {
            p = WekaClassifier.N7bdb036765(i);
        }
        return p;
    }
    static double N7bdb036765(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 92.054095) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() > 92.054095) {
            p = 1;
        }
        return p;
    }
    static double N67bc9eb766(Object []i) {
        double p = Double.NaN;
        if (i[24] == null) {
            p = 2;
        } else if (((Double) i[24]).doubleValue() <= 6.30762) {
            p = WekaClassifier.N1058359667(i);
        } else if (((Double) i[24]).doubleValue() > 6.30762) {
            p = WekaClassifier.Ncb8cfe969(i);
        }
        return p;
    }
    static double N1058359667(Object []i) {
        double p = Double.NaN;
        if (i[29] == null) {
            p = 1;
        } else if (((Double) i[29]).doubleValue() <= 3.686832) {
            p = 1;
        } else if (((Double) i[29]).doubleValue() > 3.686832) {
            p = WekaClassifier.Nf26133c68(i);
        }
        return p;
    }
    static double Nf26133c68(Object []i) {
        double p = Double.NaN;
        if (i[15] == null) {
            p = 2;
        } else if (((Double) i[15]).doubleValue() <= 9.498281) {
            p = 2;
        } else if (((Double) i[15]).doubleValue() > 9.498281) {
            p = 1;
        }
        return p;
    }
    static double Ncb8cfe969(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 25.820396) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 25.820396) {
            p = WekaClassifier.N3959d08570(i);
        }
        return p;
    }
    static double N3959d08570(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 1107.920001) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 1107.920001) {
            p = WekaClassifier.N3788e92d71(i);
        }
        return p;
    }
    static double N3788e92d71(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 152.311347) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 152.311347) {
            p = 2;
        }
        return p;
    }
    static double N7d84a5d672(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 94.644086) {
            p = WekaClassifier.Nfa3c4fb73(i);
        } else if (((Double) i[1]).doubleValue() > 94.644086) {
            p = WekaClassifier.N4568e1c176(i);
        }
        return p;
    }
    static double Nfa3c4fb73(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 5.811942) {
            p = WekaClassifier.N48d942c274(i);
        } else if (((Double) i[10]).doubleValue() > 5.811942) {
            p = WekaClassifier.N2fadf3eb75(i);
        }
        return p;
    }
    static double N48d942c274(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 2;
        } else if (((Double) i[13]).doubleValue() <= 1.646619) {
            p = 2;
        } else if (((Double) i[13]).doubleValue() > 1.646619) {
            p = 1;
        }
        return p;
    }
    static double N2fadf3eb75(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 995.387381) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 995.387381) {
            p = 1;
        }
        return p;
    }
    static double N4568e1c176(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 142.834596) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() > 142.834596) {
            p = 2;
        }
        return p;
    }
    static double N68bd698077(Object []i) {
        double p = Double.NaN;
        if (i[15] == null) {
            p = 2;
        } else if (((Double) i[15]).doubleValue() <= 33.670985) {
            p = WekaClassifier.N6b59883878(i);
        } else if (((Double) i[15]).doubleValue() > 33.670985) {
            p = 0;
        }
        return p;
    }
    static double N6b59883878(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 99.215475) {
            p = WekaClassifier.N42fe4b9779(i);
        } else if (((Double) i[1]).doubleValue() > 99.215475) {
            p = WekaClassifier.N578f489981(i);
        }
        return p;
    }
    static double N42fe4b9779(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() <= 17.694063) {
            p = WekaClassifier.N4514c8dd80(i);
        } else if (((Double) i[11]).doubleValue() > 17.694063) {
            p = 2;
        }
        return p;
    }
    static double N4514c8dd80(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 11.344381) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() > 11.344381) {
            p = 0;
        }
        return p;
    }
    static double N578f489981(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 935.840536) {
            p = WekaClassifier.N7fe761b782(i);
        } else if (((Double) i[0]).doubleValue() > 935.840536) {
            p = WekaClassifier.N2fc076fb83(i);
        }
        return p;
    }
    static double N7fe761b782(Object []i) {
        double p = Double.NaN;
        if (i[23] == null) {
            p = 2;
        } else if (((Double) i[23]).doubleValue() <= 10.324009) {
            p = 2;
        } else if (((Double) i[23]).doubleValue() > 10.324009) {
            p = 1;
        }
        return p;
    }
    static double N2fc076fb83(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 35.095105) {
            p = WekaClassifier.N4c3713b84(i);
        } else if (((Double) i[64]).doubleValue() > 35.095105) {
            p = 2;
        }
        return p;
    }
    static double N4c3713b84(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 1131.905817) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 1131.905817) {
            p = 2;
        }
        return p;
    }
    static double N3b3edaee85(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 34.738829) {
            p = WekaClassifier.N1b975da586(i);
        } else if (((Double) i[64]).doubleValue() > 34.738829) {
            p = 2;
        }
        return p;
    }
    static double N1b975da586(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (((Double) i[4]).doubleValue() <= 34.716535) {
            p = WekaClassifier.N6e8509c987(i);
        } else if (((Double) i[4]).doubleValue() > 34.716535) {
            p = 1;
        }
        return p;
    }
    static double N6e8509c987(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 4.601728) {
            p = WekaClassifier.N6045a3ce88(i);
        } else if (((Double) i[10]).doubleValue() > 4.601728) {
            p = 2;
        }
        return p;
    }
    static double N6045a3ce88(Object []i) {
        double p = Double.NaN;
        if (i[27] == null) {
            p = 2;
        } else if (((Double) i[27]).doubleValue() <= 0.914464) {
            p = 2;
        } else if (((Double) i[27]).doubleValue() > 0.914464) {
            p = 1;
        }
        return p;
    }
}
