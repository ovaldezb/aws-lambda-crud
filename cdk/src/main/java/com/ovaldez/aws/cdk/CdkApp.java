package com.ovaldez.aws.cdk;

import software.amazon.awscdk.App;

public class CdkApp {
    public static void main(final String[] args) {
        App app = new App();

        new CdkStack(app, "CdkStack");

        app.synth();
    }
}

