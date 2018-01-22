package me.kranznico.rsaswing;

import java.math.BigInteger;

/**
 * @author Nico Kranz
 *
 */
public class RSA {

	private BigInteger n = null;
	private BigInteger e = null;
	private BigInteger phi = null;
	private BigInteger d = null;

	//	public static void main(String args[])
	//	{
	//		RSA rsa = new RSA(new BigInteger("5915587277"),new BigInteger("48112959837082048697"));
	//		rsa.decrypt(rsa.encrypt("NSA better watch out! "));
	//	}

	/**
	 * Erstellt ein RSA-Modul mit zuf�lligen Primzahlen und zuf�lligem E
	 * 
	 */
	public RSA()
	{
		BigInteger p = Prime.generate();
		BigInteger q = Prime.generate();
		n = p.multiply(q);
		this.phi  = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
		e = createE(phi);
		d = ExtendedEuclidianAlgorithm.gcd(e, phi)[1];
	}
	/**
	 * Erstellt ein RSA-Modul nach gegebenen Primzahlen und ermittelt ein zuf�lliges E.
	 * @param p Eine Primzahl
	 * @param q Eine Primzahl
	 */
	public RSA(BigInteger p, BigInteger q)
	{
		this.n = p.multiply(q);
		this.phi  = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
		this.e = createE(phi);
		this.d = ExtendedEuclidianAlgorithm.gcd(e, phi)[1];
	}
	/**
	 * Erstellt ein RSA-Modul nach gegebenem e und zweier Primzahlen
	 * @param p Eine Primzahl
	 * @param q Eine Primzahl
	 * @param e Eine zu Phi teilerfremde Zahl.
	 */
	public RSA(BigInteger p, BigInteger q, BigInteger e)
	{
		n = p.multiply(q);
		this.phi  = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
		this.e = e;
		d = ExtendedEuclidianAlgorithm.gcd(e, phi)[1];
	}
	/**
	 * Gibt das Produkt der beiden Primzahlen zur�ck.
	 * @return p*q = n
	 */
	public String getN()
	{
		return n.toString();
	}

	/**
	 * Ermittelt eine zuf�llige, zu phi teilerfremde Pirmzahl.
	 * @param phi Phi von p und q. Gilt als Grenze und als modul.
	 * @return Gibt das E f�r den �ffentlichen Schl�ssel zur�ck.
	 */
	private BigInteger createE(BigInteger phi) {
		BigInteger primefac = Prime.generate(phi.toString());
		if(ExtendedEuclidianAlgorithm.gcd(primefac, phi)[0].compareTo(BigInteger.ONE) != 0 && primefac.compareTo(phi) == 1)
			return createE(phi);
		else
			return primefac;
	}
	/**
	 * Verschl�sselt eine Nachricht nach dem gegebenen RSA-Modul.
	 * @param message Eine beliebige Nachricht.
	 * @return Eine verschl�sselte Nachricht.
	 */

	public String encryptBlock(String message)
	{

		String ciphertext = "";
		String[] sa = message.split("(?<=\\G...)");
		BigInteger[] bia = new BigInteger[sa.length*3];
		char[] saca = new char[3];
		int count = 0;
		for(int i = 0; i < sa.length; i++)
		{
			char[] ca = sa[i].toCharArray();
			for(int x = 0; x < ca.length; x++)
			{
				BigInteger v = new BigInteger(Integer.toBinaryString(ca[x]));
				ciphertext += (v.modPow(e, n).toString() + " ");
			}
		}
		return ciphertext;
	}

	public String decryptBlock(String message)
	{
		return null;
	}
	
	public String encrypt(String message)
	{
		String ciphertext = "";
		for(int i = 0; i < message.length(); i++)
		{
			BigInteger v = new BigInteger(Integer.toString(message.codePointAt(i)));
			ciphertext += (v.modPow(e, n).toString() + " ");
		}
		return ciphertext;
	}
	/**
	 * Gibt den privaten Schl�ssel aus.
	 * @return privateKey(d, n)
	 */
	public BigInteger[] getPrivateKey()
	{
		BigInteger[] privateKey = new BigInteger[2];
		privateKey[0] = n;
		privateKey[1] = d;
		return null;
	}
	/**
	 * Gibt den �ffentlichen Schl�ssel aus.
	 * @return publicKey(e,n)
	 */
	public BigInteger[] getPublicKey()
	{
		BigInteger[] publicKey = new BigInteger[2];
		publicKey[0] = n;
		publicKey[1] = e;
		return publicKey;
	}
	/**
	 * Entschl�sselt den verschl�sselten Text nach RSA-Modul.
	 * @param ciphertext Eine RSA-verschl�sselte Nachricht
	 * @return Eine Nachricht als Klartext.
	 */
	public String decrypt(String ciphertext)
	{
		String cleartext = "";
		String[] cipherarr = ciphertext.split(" ");
		for(int i = 0; i < cipherarr.length; i++)
		{
			BigInteger v = new BigInteger(cipherarr[i]);
			cleartext += new String(v.modPow(d, n).toByteArray());
		}
		return cleartext;
	}
	/**
	 * Gibt das E des �ffentlichen Schl�ssels aus.
	 * @return e
	 */
	public String getE() {
		return e.toString();
	}

	/**
	 * Gibt das Phi von p und q aus.
	 * @return phi(p*q)
	 */
	public String getPhi() {
		return phi.toString();
	}
}
