/**
 * Created by luobotao on 16/8/22.
 */
function getAesString(dataTemp,keyTemp,ivTemp){//加密
    var key  =  CryptoJS.enc.Utf8.parse(keyTemp);
    var iv   =  CryptoJS.enc.Utf8.parse(ivTemp);
    var data   =  CryptoJS.enc.Utf8.parse(dataTemp);
    var encrypted = CryptoJS.AES.encrypt(data,key,
        {
            iv:iv,
            mode:CryptoJS.mode.CBC,
            padding:CryptoJS.pad.Pkcs7
        });
    return encrypted.toString();
}
function getDAesString(dataTemp,keyTemp,ivTemp){//解密
    var key  =  CryptoJS.enc.Utf8.parse(keyTemp);
    var iv   =  CryptoJS.enc.Utf8.parse(ivTemp);
    var data   =  CryptoJS.enc.Utf8.parse(dataTemp);
    var decrypted = CryptoJS.AES.decrypt(data,key,
        {
            iv:iv,
            mode:CryptoJS.mode.CBC,
            padding:CryptoJS.pad.Pkcs7
        });
    return decrypted.toString(CryptoJS.enc.Utf8);
}
function getAES(data){ //加密
    var key  = '0807060504030201';  //密钥
    var iv   = '0807060504030201';
    var encrypted = getAesString(data,key,iv); //密文
    return encrypted;
}

function getDAes(encrypted){//解密
    var key  = '0807060504030201';
    var iv   = '0807060504030201';
    var decryptedStr = getDAesString(encrypted,key,iv);
    return decryptedStr;
}
function getSha1String(data,key){//加密
    var key  = CryptoJS.enc.Hex.parse(key);
    var encrypted = CryptoJS.SHA1(data,key);
    return encrypted;
}
function getSha1(encrypted){//解密
    var key  = '1234567812345678';
    var decryptedStr = getSha1String(encrypted,key);
    return decryptedStr;
}