package com.project.ui;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class BarcodeScannerTest extends TestCase {
    
    private static final String TEST_BARCODE = "223456789";
    
    private BarcodeScannerComponent scanner;
    private String output;

    @Before
    public void setUp() {
        scanner = new BarcodeScannerComponent();
        output = "";
    }
    
    @Test
    public void testWithoutCallback() {
        scanner.simulateBarcodeScan(TEST_BARCODE);
        assertEquals(output, "");
    }

    @Test
    public void testWithCallback() {
        scanner.onBarcodeScanned(s -> output = s);
        scanner.simulateBarcodeScan(TEST_BARCODE);
        assertEquals(output, TEST_BARCODE);
    }
}
