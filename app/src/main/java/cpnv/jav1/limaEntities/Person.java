package cpnv.jav1.limaEntities;

import java.util.Calendar;
import java.util.Date;

import cpnv.jav1.lima.LimaDb;
import cpnv.jav1.lima.LimaException;

/**
 * Created by Xavier on 07.05.15.
 */
public abstract class Person {

    //========================== Attributes =============================
    protected String _lastName;
    protected String _firstName;
    protected String _initials;
    protected Date _birthDate; // setter is virtual -> subclass must access this attribute

    //========================== Internal variables =============================

    private int idperson;

    //========================== Constructor =============================

    public Person()
    {
        // empty
    }

    // must be invoked by derived classes because the class is abstract
    public Person(int id) throws LimaException
    {
        LimaDb limaDb = new LimaDb("http://192.168.0.10/");
        if (limaDb.connectionIsOK())
        {
            if (limaDb.executeQuery("SELECT personfirstname, personlastname, personinitials, birthdate FROM person WHERE idperson="+id) > 0)
            {
                limaDb.moveNext();
                setfirstName(limaDb.getField("personfirstname"));
                setlastName(limaDb.getField("personlastname"));
                String initialsFromDB = limaDb.getField("personinitials");
                if (!initialsFromDB.equals(_initials)) // Major inconsistency
                    throw new LimaException("Initials inconstency!");
                idperson = id;
            }
            else
                throw new LimaException("person with id "+id+" not found");
        }
        else
            throw new LimaException("Unable to connect to db");

    }
    //========================== Public methods =============================

    public String dump()
    {
        return getfirstName() + " " + getlastName();
    }

    public abstract void setBirthDate(Date birthDate) throws LimaException; // pure virtual

    //========================== Private methods =============================

    //========================== getter/setter =============================
    public String getlastName()
    {
        return _lastName;
    }

    public void setlastName(String lastName) throws LimaException
    {
        if (lastName.length() < 2)
            throw new LimaException("lastName is too short (" + lastName + ")");
        else
            this._lastName = lastName.toUpperCase(); // requirement: lastname in uppercase
    }

    public String getfirstName()
    {
        return _firstName;
    }

    public String getInitials()
    {
        return _initials;
    }

    public void setfirstName(String firstName) throws LimaException
    {
        if (firstName.length() < 2)
            throw new LimaException("firstName is too short (" + firstName + ")");
        else
            this._firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1); // requirement: first letter capitalized
    }

    public Date getbirthDate()
    {
        return _birthDate;
    }

}
