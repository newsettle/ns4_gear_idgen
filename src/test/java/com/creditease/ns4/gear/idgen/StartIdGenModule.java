package com.creditease.ns4.gear.idgen;

import com.creditease.ns.transporter.context.XmlAppTransporterContext;

public class StartIdGenModule {
    public static void main(String[] args) throws Exception {
        System.setProperty("configfile", "ns4.xml");
        XmlAppTransporterContext.main(new String[]{});
    }
}
