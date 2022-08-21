import {entropyToMnemonic, wordlists} from 'bip39'

export const generateMnemonicFromAddress = function (address: string) {
    return entropyToMnemonic(address, wordlists.english)
        .split(' ')
        .slice(0, 3)
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ');
}
