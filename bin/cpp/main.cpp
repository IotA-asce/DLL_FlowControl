#include<bits/stdc++.h>
using namespace std;

// Returns XOR of 'a' and 'b'
// (both of same length)
string xor1(string a, string b)
{
	
	// Initialize result
	string result = "";
	
	int n = b.length();
	
	// Traverse all bits, if bits are
	// same, then XOR is 0, else 1
	for(int i = 1; i < n; i++)
	{
		if (a[i] == b[i])
			result += "0";
		else
			result += "1";
	}
	return result;
}

// Performs Modulo-2 division
string mod2div(string divident, string divisor)
{
	
	// Number of bits to be XORed at a time.
	int pick = divisor.length();
	
	// Slicing the divident to appropriate
	// length for particular step
	string tmp = divident.substr(0, pick);
	
	int n = divident.length();
	
	while (pick < n)
	{
		if (tmp[0] == '1')
		
			// Replace the divident by the result
			// of XOR and pull 1 bit down
			tmp = xor1(divisor, tmp) + divident[pick];
		else
		
			// If leftmost bit is '0'.
			// If the leftmost bit of the dividend (or the
			// part used in each step) is 0, the step cannot
			// use the regular divisor; we need to use an
			// all-0s divisor.
			tmp = xor1(std::string(pick, '0'), tmp) +
				divident[pick];
				
		// Increment pick to move further
		pick += 1;
	}
	
	// For the last n bits, we have to carry it out
	// normally as increased value of pick will cause
	// Index Out of Bounds.
	if (tmp[0] == '1')
		tmp = xor1(divisor, tmp);
	else
		tmp = xor1(std::string(pick, '0'), tmp);
		
	return tmp;
}

// Function used at the sender side to encode
// data by appending remainder of modular division
// at the end of data.
void encodeData(string data, string key)
{
	int l_key = key.length();
	
	// Appends n-1 zeroes at end of data
	string appended_data = (data +
							std::string(
								l_key - 1, '0'));
	
	string remainder = mod2div(appended_data, key);
	
	// Append remainder in the original data
	string codeword = data + remainder;
	cout << "Remainder : "
		<< remainder << "\n";
	cout << "Encoded Data (Data + Remainder) :"
		<< codeword << "\n";
}

// Driver code
int main()
{
	// string data = "100100";
	// string key = "1101";
	string data = "0000000000000000000000000000000010000011010001011011000100010001000000111010101110110100000111111000000111011101100000000101011011011101101001101011001011111110110011100010111110111011010101100000110111110100010101101101011101101110001110110010011000110000001000101110100110100000000011000101000110100111100110101000011011011100000100010100010110111000111010010111101010101000011000100101110111100110101110001010111110100110001011101011110110100100011011111111011000001000111110001011110011101010000110111010001110110011000101000010101111111010000101010010111111100011100110100010111101111100111100000010011001010100110101100011111001001101000110001111010111010100010000001101010001011100101101100011001010100111101010101000111100111000011000001010000001010111011100011000010110011001111011010011010100110110111100110111010110111011111100001100101001010101100111000110000010000101100001010100111000010000001001001111001101110110001000011111111001110000000000111011010010110101011010110111101100001000101101001010100100110110110110100100010110010011110011100001001101111110010000010110011100110000100000110110010011000000101111100111111111100100001000001100000000100011100001111001010010000000111100000101001010100011100001101010000110101000001010100101011000111110011011110010111010010101000101011101011101110110101010010001110110010010001010011101100011110000001101110100111001001001010000101110010110000101111011111001101100111110110000000101001010111000100001001000110110100010000100110111110001001101100000100111101100010101001000111001111111001011010110000011001000010100110011001101111111010110011100001000001111101110000100000000000110000001010101011110001111000111011011011110000101000010111000000001100011000000011101011000110011110101101000101010000111011101000011000010011100011101001100011000010100101000110100011001111011100110011110101000111101101100101000001100000111100001101000111110011011110011101000010111110000101000001101001010000011010111010100100010111000011010000110011101000100101110010010101101000101011110001010100101000000101001010100010100110000111001111111110000010001100101011001010110111001001110001110110101001001111110101010110001000110100101011010011100010101111000001100010010100001111111101001000001110010001100000010111011000101000110010011001001010011000001100011100011111001000011011000101010011101110110111011001000100111111110110010111101010111100101100011100011110000100010101100100101111111001010000011110000011001101101100000111000010011010011010101000011000100101000000000100011101111100011101110101011011111100011101001100001100001111001000000111011001010011001010100110100100101111001001010100000011001101011010100010111000100010111101001110111001110011011111001100011111000101010111011001010110000101110101100010000101001010110000100110001100100001111101011100110011111001010100011101101010010000111100011100011010110001101110011001110010011010000110000000110010111100100000010110111101100110000110100000011110010101010001110001011110000100100010000010111001000010100111110110011011000101011011010000001100111101100101111101101110111111110101101001111110111001000001011100101111111111011101001110001110001111111011110010101001010111111100010110001000100100111011100101110011001111011111100011111111011100011101011000111110010011100010001101111000110010110110010010011010001100001101110000000111100110000000001011101000110001011001110000001001111011110010100101100010011111101010011111011111101000110001110010001110000001000001001100001110111101011001000110101010001011000000010111110111000110000111011000111100000001101010101011110000011101011101000011110101010000111100111001110101011011010101111001011011000100010101011010111011111001010101111011101000101110110011000000011010111111011101000111001110111000010111110001100111011101110111001011011111110011011100011000110010001010110101010111001011100001111000001011011011101010010010010101100000010101101010011101001010101011100111011001011100110010110000000001110111001110100100011111011011111100111011001111100011010001010110011101000101101111111001100011111011011001100001110001110100011101011011100000101000100010010001000110010001010111101011001010011101111101000000000100010000001011111111010110111010101011010110111100000101010101111011000001011000000110101011111101101101111001001101001110111100111110111101101101010110001100011110101011100110010100101000001011011100000111011100110111001110001101111010010000111010001111101100100000000011110001001010100010011011101000110111010100101011111101111100010010010011100110100001011100011000011110111010010111110101111100001111010000001100101010011110100011110110010010000011110011000100101010010101001111001110001111001100110011000010011001111101101111111111011110110101011001011100110010001011110010000000000010110111000010101110111111110110100100011010101000100000111101100110101100000110010101100001101011010100000111011001111100000111011111001000100111101100100100100010000100010110110001101010011001111011011100101011100001100100100101111110010101010000001000011000110101111010011110000011101001101010000101010010110110010110111101100111010111110001110111010001010101011100011110000110001010011000100001001010011100111111011010110100011111010101100011110010010101010111011101100000011111111000010010001100110101101011001011101000000110000100011001010111110101110000011010100110111011110100101111101110001000110010001010001110001110011001011011011001000010100011101001100110000010111000101001001100110010110111111100000011001001110110100110000010100100101110001111100101010110100110010010000010101001110011011101110110010101101000110000000011110001010000111011001100001010111010010111101100101001101111010110111010111000100011000011010011100100000001001001001101111000101010100101111010110001111111010110110000111001111000100100001111111001111110101111010101111110010100010000100111111110011001111110101101010010000101100100101110011100100100111010001100101100011111001010110010110011101101111100101001101001010111111111111010010101001011100010100011111100110011111101011011100010000011110011100101000100011001010110110000101111110111000111111001010011111110010001001111110010111110000101111100111110100010000101110011010100011000100010111011010001010011111000111100010010010111110000001000010110011111001011110100001001110000111010010100110000000000011110000000010010100001011110110001011001100101101111101010111011010010000001010011011010110001110001000100101011100110101100111000100111110111001100010001001010101000100001100101101110010100100110101111000110010101000100100100100010011011100111101001111111100001110110101111010010101101011110110011111010101011011010111010001010100100110100010100000100101011010100001111110001100100010011011100001010111101110011101100101101110110001111001000010111010110110011101010011011001101010110111001001010101101110111100001010110000000111101011001011010110111100010111101111111111110100001110101000100101100100101100010110011001011110101010001100010111010000110101100100001010100011011010100111110000011110100010011111101000011100001101101001011010001100011111101000001010000111110011011000000111111000110101101010101001111001110110101101010000100101000000001110001101101010001100110011001001100000100011100110111001111001000010010111001110100101100100110110011100100111111000011011010010111001011001001010100010";
	string key = "100000100110000010001110110110101";
	
	encodeData(data, key);
	
	return 0;
}

// This code is contributed by MuskanKalra1
