package com.example.zzz.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Controller
public class PayController {

//    应用id
    @Value("${alipay_test.APP_ID}")
    private String APP_ID;
//    应用私钥
    @Value("${alipay_test.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;

//    编码
    @Value("${alipay_test.CHARSET}")
    private String CHARSET;

//  支付宝公钥
    @Value("${alipay_test.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;

//  接口路径
    @Value("${alipay_test.GATEWAY_URL}")
    private String GATEWAY_URL;

//    格式
    @Value("${alipay_test.FORMAT}")
    private String FORMAT;


    //签名方式
    @Value("${alipay_test.SIGN_TYPE}")
    private String SIGN_TYPE;

    // 异步请求，需要使用域名
    @Value("${alipay_test.NOTIFY_URL}")
    private  String NOTIFY_URL;

    // 同步请求，不需要域名
    @Value("${alipay_test.RETURN_URL}")
    private  String RETURN_URL;


    @RequestMapping(value = {"/toBuy","/"})
    private String toBuy(){
        return "buy";
    }

//    @RequestMapping("/alipay_buy")
//    public void alipay_buy(Book book, HttpServletResponse httpResponse) throws IOException {
//
//        Random r = new Random();
//        //实例化客户端,填入所需参数
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
//        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//        //在公共参数中设置回跳和通知地址
//        request.setReturnUrl(RETURN_URL);
//        request.setNotifyUrl(NOTIFY_URL);
//
//        //商户订单号，商户网站订单系统中唯一订单号，必填
//        //生成随机Id   商户订单号，商户网站订单系统中唯一订单号，必填
//        String out_trade_no = UUID.randomUUID().toString();
//        //付款金额，必填
////        String total_amount = Integer.toString(r.nextInt(9999999) + 1000000);
//        //订单名称，必填
////        String subject = "奥迪A8 2016款 A8L 60 TFSl quattro豪华型";
//        //商品描述，可空
//
//        // 可填，延时时间，如果填了在此时间段内没有付款则无效，这里的时间支付宝界面上减2分钟
//        long etime1 = System.currentTimeMillis() + 3 * 60 * 1000;//延时函数，单位毫秒，这里是延时了3分钟
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        String time_expire = df.format(new Date(etime1));
//        String num = df.format(new Date());
////        String body = "尊敬的会员欢迎购买奥迪A8 2016款 A8L 60 TFSl quattro豪华型";
//        request.setBizContent("{\"out_trade_no\":\"" + num + "\","
//                + "\"total_amount\":\"" + book.getPrice() + "\","
//                + "\"subject\":\"" + book.getBookName() + "\","
//                + "\"body\":\"" + book.getDesc() + "\","
//                + "\"time_expire\":\"" + time_expire + "\","
//                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//        String form = "";
//        try {
//            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        httpResponse.setContentType("text/html;charset=" + CHARSET);
//        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();
//    }
    @RequestMapping("/alipay_ajax_buy")
    @ResponseBody
    public String alipay_ajax_buy(@RequestParam("bookName") String bookName,@RequestParam("desc")String desc,@RequestParam("price")Double price, HttpServletResponse httpResponse,HttpServletRequest httpServletRequest) throws IOException {

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        //付款金额，必填
        //订单名称，必填
        //商品描述，可空

        // 可填，延时时间，如果填了在此时间段内没有付款则无效，这里的时间支付宝界面上减2分钟
        long etime1 = System.currentTimeMillis() + 3 * 60 * 1000;//延时函数，单位毫秒，这里是延时了3分钟
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String time_expire = df.format(new Date(etime1));
        String num = df1.format(new Date());
        request.setBizContent("{\"out_trade_no\":\"" + num + "\","
                + "\"total_amount\":\"" + price + "\","
                + "\"subject\":\"" + bookName + "\","
                + "\"body\":\"" + desc + "\","
                + "\"time_expire\":\"" + time_expire + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    @RequestMapping(value = "/returnUrl", method = RequestMethod.GET)
    public String returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        System.out.println("=================================同步回调=====================================");

        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }

        System.out.println(params);//查看参数都有哪些
        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
        //验证签名通过
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号=" + out_trade_no);
            System.out.println("支付宝交易号=" + trade_no);
            System.out.println("付款金额=" + total_amount);

            //支付成功，修复支付状态
//            payService.updateById(Integer.valueOf(out_trade_no));
            return "success";//跳转付款成功页面
        } else {
            return "fail";//跳转付款失败页面
        }

    }

}


