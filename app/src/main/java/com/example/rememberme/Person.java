package com.example.rememberme;

public class Person {
    protected String name;
    protected String bDay;
    protected String addressLineOne;
    protected String addressLineTwo;
    protected String city;
    protected String state;
    protected String zipCode;

    protected Person(String name, String bDay, String addressLineOne, String addressLineTwo, String city, String state, String zipCode)
    {
        this.name = name;
        this.bDay = bDay;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String toString()
    {
        String personString = name + "," + bDay + "," + addressLineOne + "," + addressLineTwo + "," + city + "," + state + "," + zipCode;
        return personString;
    }

    protected String getName()
    {
        return name;
    }
    protected String getBDay()
    {
        return bDay;
    }
    protected String getAddressLineOne()
    {
        return addressLineOne;
    }
    protected String getAddressLineTwo()
    {
        return addressLineTwo;
    }
    protected String getCity()
    {
        return city;
    }
    protected String getState()
    {
        return state;
    }
    protected String getZipCode()
    {
        return zipCode;
    }
    protected void setName(String name)
    {
        this.name = name;
    }
    protected void setBDay(String bDay)
    {
        this.bDay = bDay;
    }
    protected void setAddressLineOne(String addr1)
    {
        this.addressLineOne = addr1;
    }
    protected void setAddressLineTwo(String addr2)
    {
        this.addressLineTwo = addr2;
    }
    protected void setCity(String city)
    {
        this.city = city;
    }
    protected void setState(String state)
    {
        this.state = state;
    }
    protected void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }
}
