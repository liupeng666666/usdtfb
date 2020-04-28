package com.whp.usdtfb.block.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.block.Dao.FbSysWalletDao;
import com.whp.usdtfb.block.Dao.FbUsdtDao;
import com.whp.usdtfb.block.Dao.FbWalletDao;
import com.whp.usdtfb.block.Interface.EthInterface;
import com.whp.usdtfb.block.Utils.Web3jClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : 张吉伟
 * @data : 2018/12/17 21:12
 * @descrpition :
 */
@Service
public class EthImpl implements EthInterface {

    @Autowired
    private FbWalletDao fbWalletDao;
    @Autowired
    private FbSysWalletDao fbSysWalletDao;
    @Autowired
    private FbUsdtDao fbUsdtDao;

    private static Web3j web3j = Web3jClient.getClient();
    private static Admin admin = Web3jClient.getAdmin();
    private int gaodu = 314159;
    private String password = "123456";
    private String filePath = "D://wallet/";

    private BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    private BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    @Override
    public JSONObject new_address(String userid) {
        String address;
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("codeid", 1);
            map.put("userid", userid);
            Map<String, Object> fmap = fbWalletDao.FbWalletSelect(map);
            if (fmap == null) {
                String fhui = WalletUtils.generateNewWalletFile(password, new File(filePath), false);
                Credentials credentials = WalletUtils.loadCredentials(password, filePath + fhui);
                address = credentials.getAddress();
                json.put("address", address);
                map.put("address", address);
                map.put("private", credentials.getEcKeyPair().getPrivateKey());
                map.put("public", credentials.getEcKeyPair().getPublicKey());
                map.put("path", filePath + fhui);
                fbWalletDao.FbWalletInsert(map);
            } else {
                json.put("address", fmap.get("address"));
            }
            json.put("code", 100);

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public BigInteger getBalance(String accountId) {
        try {
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(web3j.ethBlockNumber().send().getBlockNumber());
            EthGetBalance ethGetBalance = web3j.ethGetBalance(accountId, defaultBlockParameter).send();
            if (ethGetBalance != null) {
                return ethGetBalance.getBalance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void XunHuan() {
        String value = RedisUtils.get("ETH", 7);
        if (value == null || value == "") {
            RedisUtils.set("ETH", gaodu + "", 7);
        } else {
            gaodu = Integer.parseInt(value);
        }
        BigInteger bigInteger = getCurrentBlockNumber();
        System.out.println("高度：" + bigInteger);
        List<Map<String, Object>> wallet_list = fbWalletDao.FbWalletList(1);
        for (BigInteger i = BigInteger.valueOf(gaodu); i.compareTo(bigInteger) < 0; i = i.add(BigInteger.ONE)) {
            EthBlock ethBlock = getBlockEthBlock(i.intValue());
            if (ethBlock != null) {
                System.out.println("hash2:" + ethBlock.getBlock().getHash());
                List<EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
                if (list != null) {
                    list.forEach(tx -> {
                        EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx.get();
                        System.out.println(transaction.getBlockNumber());
                        System.out.println(transaction.getFrom());
                        System.out.println(transaction.getTo());
                        System.out.println(transaction.getValue());
                        for (Map<String, Object> map : wallet_list) {
                            if (transaction.getTo() != null && transaction.getTo().equals(map.get("address"))) {
                                //获得交易
                                BigDecimal money = toDecimal(18, transaction.getValue());
                                zhuanzhang(transaction.getTo(), ethBlock.getBlock().getHash(), transaction.getFrom(), money, transaction.getBlockNumber().intValue(), map.get("private").toString(), map.get("userid").toString());
                                break;
                            }
                        }

                    });
                }


            }
        }
    }


    /**
     * 获得当前区块高度
     *
     * @return 当前区块高度
     * @throws IOException
     */
    public BigInteger getCurrentBlockNumber() {
        try {
            Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
            return request.send().getBlockNumber();
        } catch (Exception e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        }
    }

    /**
     * 获得ethblock
     *
     * @param blockNumber 根据区块编号
     * @return
     * @throws IOException
     */
    public EthBlock getBlockEthBlock(Integer blockNumber) {
        EthBlock ethBlock = null;
        try {
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(blockNumber);
            Request<?, EthBlock> request = web3j.ethGetBlockByNumber(defaultBlockParameter, true);
            ethBlock = request.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ethBlock;
    }

    /**
     * 根据hash值获取交易
     *
     * @param hash
     * @return
     * @throws IOException
     */
    public EthTransaction getTransactionByHash(String hash) {
        try {
            Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(hash);
            return request.send();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public EthGetTransactionReceipt ethGetTransactionReceipt(String hash) {
        try {
            Request<?, EthGetTransactionReceipt> request = web3j.ethGetTransactionReceipt(hash);
            return request.send();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void JiaoYi(String private_name, String from_address, String to_address, BigDecimal money) {
        try {
            Credentials credentials = Credentials.create(private_name);
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    from_address, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            BigInteger value = Convert.toWei(money, Convert.Unit.ETHER).toBigInteger();
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, GAS_PRICE, GAS_LIMIT, to_address, value);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            //发送交易
            EthSendTransaction ethSendTransaction =
                    web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();

            //归集成功
            System.out.println("=====归集成功=====");
            System.out.println(transactionHash);
            System.out.println("金额：" + money);
            System.out.println("=========**========");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @param address
     * @param hash
     * @param from_address
     * @param value
     * @param block
     * @param private_name
     */
    public void zhuanzhang(String address, String hash, String from_address, BigDecimal value, int block, String private_name, String userid) {
        try {
            Request<?, EthGetBalance> request = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest"));
            EthGetBalance ethGetBalance = request.send();
            System.out.println(ethGetBalance.getBalance());
            BigDecimal money = toDecimal(18, ethGetBalance.getBalance());
            if (value.compareTo(money) != -1) {
                //插入一条记录
                JSONObject json = new JSONObject();
                json.put("from_address", from_address);
                json.put("userid", userid);
                json.put("to_address", address);
                json.put("txid", hash);
                json.put("money", value);
                json.put("height", block);
                json.put("codeid", 1001);
                fbUsdtDao.FbUsdtInsert(json);
                Map<String, Object> sys_wallet = fbSysWalletDao.FbSysWalletSelect(1);
                if (money.subtract(value).compareTo(BigDecimal.valueOf(0.1)) > -1) {
                    JiaoYi(private_name, address, sys_wallet.get("address").toString(), value);
                } else {
                    JiaoYi(private_name, address, sys_wallet.get("address").toString(), money.subtract(BigDecimal.valueOf(0.1)));
                }
                RedisUtils.set("ETH", block + "", 7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static BigDecimal toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        BigDecimal balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN);
        return balance;
    }

}

