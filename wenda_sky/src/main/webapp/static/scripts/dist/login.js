define(
    "nowcoder/1.2.1251/javascripts/site/login/login",
    ["nc", "cpn", "ncpc", "../../util/util"],
    function (i, n, t) {
        var e = i("nc"),
            c = i("cpn"), a = i("ncpc"), r = e.$, s = e.Sys, o = e.Base, p = e.Dom, l = e.Str, u = e.Uri, m = e.Limit,
            h = c.Input, v = c.Popup, d = a.LoginUtil, f = a.Config, w = a.Action;
        i("../../util/util");
        s.ready({
            initialize: function g() {
                var i = this, n = u.getParam("ltype");
                i.isSms = "sms" === n, i.isOversea = "oversea" === n, i.isAccount = !i.isSms && !i.isAccount, i.initCpn(), i.isSms && i.initCaptcha(), i.initSubmit(), i.initAuthMore()
            }, initCpn: function S() {
                var i = this, n = i.cpn = {};
                n.account = h.transform(r("#jsEmailIpt").parent()), !i.isSms && (n.pwd = h.transform(r("#jsPasswordIpt").parent())), i.isSms && (n.captcha = h.transform(r("#jsCaptcha").parent())), (i.isOversea || i.isSms) && (n.zip = d.initPhoneZip({renderTo: r("#jsCountry")}))
            }, initCaptcha: function C() {
                var c = this.cpn;
                d.initCaptcha({
                    btn: r("#jsSendCaptcha"), isPhone: !0, check: function (e) {
                        d.checkPhone({
                            zip: c.zip, phone: c.account, needRegister: !0, call: function (i, n) {
                                if (n.isError || n.isRegister) return e && e(!n.isError);
                                c.account.setNone();
                                var t = v.confirm("该手机还没注册，是否立即注册？", function () {
                                    window.location.href = u.addParam({
                                        zip: r.trim(c.zip.val()),
                                        phone: r.trim(c.account.val()),
                                        captcha: 1,
                                        callBack: u.getParam("callBack")
                                    }, "/register")
                                }, function () {
                                    e && e(!1)
                                });
                                t.okBtn.html("立即注册"), t.cancelBtn.html("暂不注册")
                            }
                        })
                    }, val: function () {
                        var i = r.trim(c.zip.val()), n = r.trim(c.account.val());
                        return i + n
                    }
                })
            }, initSubmit: function P() {
                var i = this;
                r("#jsLoginBtn").on("click", o.bind(i.submit, i)), r("div.input-section form").on("submit", o.bind(i.submit, i))
            }, initAuthMore: function b() {
                var i = r("#jsLoginFold");
                p.hoverToggle({
                    el: i, duration: 250, enter: function () {
                        i.addClass("open")
                    }, leave: function () {
                        i.removeClass("open")
                    }
                })
            }, val: function y(i) {
                var n = this, t = n.cpn, e = {
                    email: r.trim(t.account.val()),
                    pwd: t.pwd ? r.trim(t.pwd.val()) : "",
                    code: t.captcha ? r.trim(t.captcha.val()) : "",
                    remember: r("#jsRemLoginChk").prop("checked") ? "true" : "false"
                }, c = n.isOversea || n.isSms ? r.trim(t.zip.val()) : "";
                !i && (n.isOversea || n.isSms) && (e.zip = c), i && ((n.isOversea || n.isSms) && (e.email = c + e.email), (n.isOversea || n.isAccount) && (e.cipherPwd = n.encrypt(e.pwd)), !n.isSms && delete e.code, delete e.pwd);
                return e
            }, submit: function E(i) {
                i.preventDefault();
                var t = this, n = r("#jsLoginBtn");
                t.clear(), t.check(function (i) {
                    i && !m.clear(n) && w.login({
                        body: t.val(!0), captcha: {type: f.captcha.login}, call: function (i) {
                            var n = u.getParam("close");
                            if (n) return window.close();
                            var t = u.getParam("callBack");
                            t = t ? decodeURIComponent(window.unescape(t)) : null, window.location.href = t || "/home"
                        }, error: function (i) {
                            var n = 4 === i.code;
                            n && t.cpn.pwd.setErrorTips(i.msg || "密码不正确，请重试！"), !n && t.cpn.account.setErrorTips(i.msg)
                        }, always: function () {
                            m.clear(n)
                        }
                    })
                })
            }, clear: function j() {
                o.forEach(this.cpn, function (i) {
                    i.setNone && i.setNone()
                })
            }, check: function k(i) {
                var n = this, t = n.cpn, e = n.val(), c = e.email, a = !0;
                !n.isOversea && !n.isSms || ("+86" === e.zip ? l.isPhone(c) : /^\d{3,}$/.test(c)) ? n.isOversea || n.isSms || l.isEmail(c) || l.isPhone(c) || (t.account.setErrorTips("请输入正确的邮箱或手机"), a = !1) : (t.account.setErrorTips("请输入正确的手机号码"), a = !1);
                !n.isOversea && !n.isAccount || e.pwd || (t.pwd.setErrorTips("密码不能为空"), a = !1);
                n.isSms && !e.code && (t.captcha.setErrorTips("验证码不能为空"), a = !1);
                return i && i(a), a
            }, encrypt: function z(i) {
                var n = new window.NCJSEncrypt;
                return n.setPublicKey(r("#jsNCPublicKey").html() || ""), n.encrypt(r.trim(i))
            }
        })
    });