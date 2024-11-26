#include <iostream>  
#include <vector>  
#include <stdexcept>  
#include <cassert>  
using namespace std;

long long fact(int n) {
    long long result = 1;
    for (int i = 1; i <= n; ++i) {
        result *= i;
    }
    return result;
}

vector<long long> gen(int n) {
    if (n <= 0) {
        throw invalid_argument("Input should be a positive integer");
    }
    vector<long long> facts;
    for (int i = 0; i < n; ++i) {
        facts.push_back(fact(i));
    }
    return facts;
}

void test() {
    assert((gen(3) == vector<long long>{1, 1, 2}));
    assert((gen(5) == vector<long long>{1, 1, 2, 6, 24}));
    assert((gen(7) == vector<long long>{1, 1, 2, 6, 24, 120, 720}));
    try {
        gen(0);
    }
    catch (const invalid_argument&) {
        assert(true);
    }
}

int main() {
    try {
        int n;
        cout << "Enter a natural number: ";
        cin >> n;

        vector<long long> result = gen(n);
        for (long long& num : result) {
            cout << num << " ";
        }
        cout << "\n";

        test();
        cout << "All tests passed!" << "\n";
    }
    catch (const exception& err) {
        cerr << "Error: " << err.what() << "\n";
    }
    return 0;
}
