package org.nevec.rjm ;

import java.lang.* ;
import java.util.* ;
import java.math.* ;

/** Factored integers.
* This class contains a non-negative integer with the prime factor decomposition attached.
* @since 2006-08-14
* @since 2012-02-14 The internal representation contains the bases, and becomes sparser if few 
*    prime factors are present.
* @author Richard J. Mathar
*/
public class Ifactor implements Cloneable, Comparable<Ifactor>
{
        /**
        * The standard representation of the number
        */
        public BigInteger n ;

        /*
        * The bases and powers of the prime factorization.
        * representation n = primeexp[0]^primeexp[1]*primeexp[2]^primeexp[3]*...
        * The value 0 is represented by an empty vector, the value 1 by a vector of length 1
        * with a single power of 0.
        */
        public Vector<Integer> primeexp ;

        final public static Ifactor ONE = new Ifactor(1) ;

        final public static Ifactor ZERO = new Ifactor(0) ;

        /** Constructor given an integer.
        * constructor with an ordinary integer
        * @param number the standard representation of the integer
        * @author Richard J. Mathar
        */
        public Ifactor(int number)
        {
                n = new BigInteger(""+number) ;
                primeexp = new Vector<Integer>() ;
                if( number > 1 )
                {
                        int primindx = 0 ;
                        Prime primes = new Prime() ;
                        /* Test division against all primes.
                        */
                        while(number > 1)
                        {
                                int ex=0 ;
                                /* primindx=0 refers to 2, =1 to 3, =2 to 5, =3 to 7 etc
                                */
                                int p = primes.at(primindx).intValue() ;
                                while( number % p == 0 )
                                {
                                        ex++ ;
                                        number /= p ;
                                        if ( number == 1 )
                                                break ;
                                }
                                if ( ex > 0 )   
                                {
                                        primeexp.add(new Integer(p)) ;
                                        primeexp.add(new Integer(ex)) ;
                                }
                                primindx++ ;
                        }
                }
                else if ( number == 1)
                {
                        primeexp.add(new Integer(1)) ;
                        primeexp.add(new Integer(0)) ;
                }
        } /* Ifactor */

        /** Constructor given a BigInteger .
        * Constructor with an ordinary integer, calling a prime factor decomposition.
        * @param number the BigInteger representation of the integer
        * @author Richard J. Mathar
        */
        public Ifactor(BigInteger number)
        {
                n = number ;
                primeexp = new Vector<Integer>() ;
                if ( number.compareTo(BigInteger.ONE) == 0 )
                {
                        primeexp.add(new Integer(1)) ;
                        primeexp.add(new Integer(0)) ;
                }
                else
                {
                        int primindx = 0 ;
                        Prime primes = new Prime() ;
                        /* Test for division against all primes.
                        */
                        while(number.compareTo(BigInteger.ONE) == 1)
                        {
                                int ex=0 ;
                                BigInteger p = primes.at(primindx) ;
                                while( number.remainder(p).compareTo(BigInteger.ZERO) == 0 )
                                {
                                        ex++ ;
                                        number = number.divide(p) ;
                                        if ( number.compareTo(BigInteger.ONE) == 0 )
                                                break ;
                                }
                                if ( ex > 0 )
                                {
                                        primeexp.add(new Integer(p.intValue()) ) ;
                                        primeexp.add(new Integer(ex) ) ;
                                }
                                primindx++ ;
                        }
                }
        } /* Ifactor */

        /** Copy constructor.
        * @param oth the value to be copied
        * @author Richard J. Mathar
        */
        public Ifactor(Ifactor oth)
        {
                n = oth.n ;
                primeexp = oth.primeexp ;
        } /* Ifactor */

        /** Deep copy.
        * @since 2009-08-14
        * @author Richard J. Mathar
        */
        public Ifactor clone()
        {
                Vector<Integer> p = (Vector<Integer>)primeexp.clone();
                Ifactor cl = new Ifactor(0) ;
                cl.n = new BigInteger(""+n) ;
                return cl ;
        } /* Ifactor.clone */

        /** Comparison of two numbers.
        * The value of this method is in allowing the Vector.contains() calls that use the value,
        * not the reference for comparison.
        * @param oth the number to compare this with.
        * @return true if both are the same numbers, false otherwise.
        * @author Richard J. Mathar
        */
        public boolean equals(final Ifactor oth)
        {
                return (  n.compareTo(oth.n) == 0 ) ;
        } /* Ifactor.equals */

        /** Pulling the r-th root.
        * @param r the positive or negative (nonzero) root.
        * @return n^(1/r).
        *   The return value falls into the Ifactor class if r is positive, but if r is negative
        *   a Rational type is needed.
        * @since 2009-05-18
        * @author Richard J. Mathar
        */
        public Rational root(final int r) throws ArithmeticException
        {
                if ( r == 0 )
                        throw new ArithmeticException("Cannot pull zeroth root of "+ toString()) ;
                else if ( r < 0 )
                {
                        /* a^(-1/b)= 1/(a^(1/b))
                        */
                        final Rational invRoot = root(-r) ;
                        return Rational.ONE.divide(invRoot) ;
                }
                else
                {
                        BigInteger pows = BigInteger.ONE ;
                        for(int i=0 ; i < primeexp.size() ; i += 2)
                        {
                                /* all exponents must be multiples of r to succeed (that is, to
                                * stay in the range of rational results).
                                */
                                int ex = primeexp.elementAt(i+1).intValue() ;
                                if ( ex % r != 0 )
                                        throw new ArithmeticException("Cannot pull "+ r+"th root of "+ toString()) ;

                                pows.multiply( new BigInteger(""+primeexp.elementAt(i)).pow(ex/r) ) ;
                        }
                        /* convert result to a Rational; unfortunately this will loose the prime factorization */
                        return new Rational(pows) ;
                }
        } /* Ifactor.root */

        /** The square-free part.
        * @return the minimum m such that m times this number is a square.
        * @since 2008-10-16
        * @author Richard J. Mathar
        */
        public BigInteger core()
        {
                BigInteger resul = BigInteger.ONE ;
                for(int i=0 ; i < primeexp.size() ; i += 2)
                        if ( primeexp.elementAt(i+1).intValue() % 2 != 0)
                                resul = resul.multiply( new BigInteger(primeexp.elementAt(i).toString()) );
                return resul ;
        } /* Ifactor.core */

        /** Compare value against another Ifactor
        * @param oth The value to be compared agains.
        * @return 1, 0 or -1 according to being larger, equal to or smaller than oth.
        * @since 2012-02-15
        * @author Richard J. Mathar
        */
        public int compareTo( final Ifactor oth)
        {
                return n.compareTo(oth.n) ;
        } /* compareTo */

        /** Convert to printable format
        * @return a string of the form n:prime^pow*prime^pow*prime^pow...
        * @author Richard J. Mathar
        */
        public String toString()
        {
                String resul = new String(n.toString()+":") ;
                if ( n.compareTo(BigInteger.ONE) == 0 )
                        resul += "1" ;
                else
                {
                        boolean firstMul = true ;
                        for(int i=0 ; i < primeexp.size() ; i += 2)
                        {
                                if ( ! firstMul) 
                                        resul += "*" ;
                                if ( primeexp.elementAt(i+1).intValue()  > 1 )
                                        resul += primeexp.elementAt(i).toString()+"^"+primeexp.elementAt(i+1).toString() ;
                                else
                                        resul +=  primeexp.elementAt(i).toString() ;
                                firstMul = false ;
                        }
                }
                return resul ;
        } /* Ifactor.toString */

        /** Test program.
        * It takes a single argument n and prints the integer factorizaton.<br>
        * java -cp . org.nevec.rjm.Ifactor n<br>
        * @param args It takes a single argument n and prints the integer factorizaton.<br>
        * @author Richard J. Mathar
        */
        public static void main(String[] args)
        {
                BigInteger n = new BigInteger(args[0]) ;
                System.out.println( new Ifactor(n)) ;
        } /* Ifactor.main */
} /* Ifactor */
