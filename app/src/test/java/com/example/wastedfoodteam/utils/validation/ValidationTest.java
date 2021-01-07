package com.example.wastedfoodteam.utils.validation;

import org.junit.Assert;
import org.junit.Test;

public class ValidationTest {

//    @Test
//    public void testCheckPhoneExist() throws Exception {
//        Validation.checkPhoneExist(null, null, "phone");
//    }

    @Test
    public void testCheckPhoneCorrect() {
        boolean result = Validation.checkPhone("0984605568");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPhoneCorrect2() {
        boolean result = Validation.checkPhone("84984605568");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPhoneNumberNull() {
        boolean result = Validation.checkPhone(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneEmpty() {
        boolean result = Validation.checkPhone("");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneContainSpace() {
        boolean result = Validation.checkPhone("098460 5568");
        Assert.assertFalse(result);
    }


    @Test
    public void testCheckPhoneShorterThan9() {
        boolean result = Validation.checkPhone("098460556");
        Assert.assertFalse(result);
    }
    @Test
    public void testCheckPhoneShorterThan9By84() {
        boolean result = Validation.checkPhone("8498460556");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneLargerThan9s() {
        boolean result = Validation.checkPhone("09846055688");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneLargerThan9By84() {
        boolean result = Validation.checkPhone("849846055688");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneContainSpecialCharacter() {
        boolean result = Validation.checkPhone("@#$12456");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneContainWordCharacter() {
        boolean result = Validation.checkPhone("098460556a");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPhoneNotStart0or84() {
        boolean result = Validation.checkPhone("1984605568");
        Assert.assertFalse(result);
    }

    //checkMail

    @Test
    public void testCheckEmailNormalCorrect() {
        boolean result = Validation.checkEmail("tungphamtp987@gmail.com");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckEmailOrganizationCorrect() {
        boolean result = Validation.checkEmail("tungptse05613@fpt.edu.vn");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckEmailFailureNulls() {
        boolean result = Validation.checkEmail(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailureEmpty() {
        boolean result = Validation.checkEmail("");
        Assert.assertFalse(result);
    }
    @Test
    public void testCheckEmailFailureContainSpace() {
        boolean result = Validation.checkEmail("tung phamtp987@gmail.com");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure2() {
        boolean result = Validation.checkEmail("tungphamtp987gmail.com");
        Assert.assertFalse(result);
    }


    @Test
    public void testCheckEmailFailure3() {
        boolean result = Validation.checkEmail("tungphamtp987@@gmail.com");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure4() {
        boolean result = Validation.checkEmail("sampleemail@gmail.com.v");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure5() {
        boolean result = Validation.checkEmail("tungphamtp987@gmail");
        Assert.assertFalse(result);
    }


    @Test
    public void testCheckEmailContainSpecialCharacter() {
        boolean result = Validation.checkEmail("tungphamtp987!@gmail.com");
        Assert.assertFalse(result);
    }

    //check Pass

    @Test
    public void testCheckPasswordCorrect() {
        boolean result = Validation.checkPassword("samplepass1");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPasswordCorrect1() {
        boolean result = Validation.checkPassword("1234test");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect2() {
        boolean result = Validation.checkPassword("1234test1234test");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect3s() {
        boolean result = Validation.checkPassword("test@1234");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect4s() {
        boolean result = Validation.checkPassword("Test@1234");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPasswordFailureNulls() {
        boolean result = Validation.checkPassword(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureEmpty() {
        boolean result = Validation.checkPassword("");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureInputSpace() {
        boolean result = Validation.checkPassword("test 1234");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPasswordFailureOnlyNum() {
        boolean result = Validation.checkPassword("01234567");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureOnlyWord() {
        boolean result = Validation.checkPassword("asdfghjk");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureOnlyLargerThan16() {
        boolean result = Validation.checkPassword("asdfghjk012345678");
        Assert.assertFalse(result);
    }


    //checkName
    @Test
    public void testCheckNameCorrect() {
        boolean result = Validation.checkName("a");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameCorrect2() {
        boolean result = Validation.checkName("abc abc");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameNameIs2To49Character() {
        boolean result = Validation.checkName("Jonh Smith");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameNormalName() {
        boolean result = Validation.checkName("Phạm Thanh Tùng");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckNameHasNumberCharacter() {
        boolean result = Validation.checkName("(HN_k12) Phạm Thanh Tùng");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameEqual50() {
        boolean result = Validation.checkName("ádasdasda dasdasdasdasdasdasdasdasdasdasdasdasdas");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameLager50() {
        boolean result = Validation.checkName("assdasdasda dasdasdasdasdasdasdasdasdasdasdasdasdas");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameEmpty() {
        boolean result = Validation.checkName("");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameNull() {
        boolean result = Validation.checkName(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameSpecial() {
        boolean result = Validation.checkName("Tung@1101");
        Assert.assertTrue(result);
    }
    //check Dob
    @Test
    public void testValidateDateCorrect() {
        Boolean result = Validation.validateDate("1998-11-01");
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testValidateDateCurrentDate() {
        Boolean result = Validation.validateDate("2020-12-05");
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testValidateDateLagerCurrentDate() {
        Boolean result = Validation.validateDate("2021-11-01");
        Assert.assertEquals(Boolean.FALSE, result);
    }


}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme