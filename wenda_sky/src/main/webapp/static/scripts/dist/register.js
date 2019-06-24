define(
    "static//login/register",
    ["nc", "cpn", "ncpc", "../../action/profile", "../../core/siteConfig"],
    function (t, n, i) {
        var e = t("nc"),
            c = t("cpn"),
            r = t("ncpc"),
            a = e.$,
            o = e.Sys,
            p = e.Base, s = e.Limit, u = e.Dom, h = (e.Str, e.Uri), d = (e.Time, c.Input), l = c.Popup, f = r.LoginUtil,
            w = r.Action;
        t("../../action/profile"), t("../../core/siteConfig");
        o.ready({
            initialize: function m() {
                var t = this;
                t.initCpn(), t.initAuthMore(), t.initCaptcha(), t.initSubmit()
            }, initCpn: function v() {
                var t = this, n = t.cpn = {}, i = h.getParam("zip");
                n.zip = f.initPhoneZip({
                    renderTo: a("#jsPhoneZip"), showNow: i, zipOptionChange: function () {
                        i && t.initQuery()
                    }
                }), n.account = d.transform(a("#jsEmailIpt").parent()), n.captcha = d.transform(a("#jsCaptcha").parent()), n.pwd = d.transform(a("#jsPasswordIpt").parent()), n.pwd2 = d.transform(a("#jsPasswordIpt2").parent()), n.zip.listen("change", p.bind(t.checkRegisterAccount, t)), n.pwd.listen("blur", p.bind(t.checkRegisterPwd, t)), n.pwd2.listen("blur", p.bind(t.checkRegisterPwd, t)), n.account.listen("blur", p.bind(t.checkRegisterAccount, t))
            }, initAuthMore: function g() {
                var t = a("#jsLoginFold");
                u.hoverToggle({
                    el: t, duration: 250, enter: function () {
                        t.addClass("open")
                    }, leave: function () {
                        t.removeClass("open")
                    }
                })
            }, initCaptcha: function b() {
                var i = this;
                i.authCaptcha = f.initCaptcha({
                    btn: a("#jsSendCaptcha"),
                    isPhone: !0,
                    check: p.bind(i.checkRegisterAccount, i),
                    val: function () {
                        var t = a.trim(i.cpn.zip.val()), n = a.trim(i.cpn.account.val());
                        return t + n
                    }
                })
            }, initSubmit: function C() {
                var t = this;
                a("#jsRegisterBtn").on("click", p.bind(t.submit, t)), a("div.input-section form").on("submit", p.bind(t.submit, t))
            }, initQuery: function P() {
                var t = this, n = t.cpn, i = h.getParams();
                window.setTimeout(function () {
                    i.zip && n.zip.val(i.zip), i.phone && n.account.val(i.phone), "1" === i.captcha && t.authCaptcha && t.authCaptcha.send()
                }, 1e3)
            }, val: function k(t) {
                var n = this.cpn, i = a.trim(n.zip.val()) || "+86", e = a.trim(n.account.val()), c = {
                    zip: i,
                    phone: (t ? i : "") + e,
                    code: a.trim(n.captcha.val()),
                    password: a.trim(n.pwd.val()),
                    pwd2: a.trim(n.pwd2.val())
                };
                t && (c.cipherPwd = this.encrypt(c.password), delete c.zip, delete c.password, delete c.pwd2);
                return c
            }, submit: function z(t) {
                t.preventDefault();
                var i = this, n = a("#jsRegisterBtn");
                i.check(function (t) {
                    t && !s.mark(n) && w.register({
                        body: i.val(!0), call: function (t) {
                            var n = h.getParam("callBack");
                            n = n ? decodeURIComponent(window.unescape(n)) : null, window.location.href = n || "/completeness"
                        }, error: function (t) {
                            var n = t.msg || "";
                            -1 !== n.indexOf("密码") ? (i.cpn.pwd.setErrorTips(n), i.cpn.pwd2.setErrorTips(n)) : -1 !== n.indexOf("验证码") ? i.cpn.captcha.setErrorTips(n) : l.alert(n)
                        }, always: function () {
                            s.clear(n)
                        }
                    })
                })
            }, clear: function R() {
                p.forEach(this.cpn, function (t) {
                    t.setNone && t.setNone()
                })
            }, check: function y(n) {
                var t = this, i = t.val(), e = t.cpn, c = (t.isRegister, !0);
                i.code || (e.captcha.setErrorTips("验证码不能为空"), c = !1);
                return c = t.checkRegisterPwd(!0) && c, t.checkRegisterAccount(function (t) {
                    n && n(c && t)
                })
            }, checkRegisterAccount: function j(t) {
                f.checkPhone({zip: this.cpn.zip, phone: this.cpn.account, call: t})
            }, checkRegisterPwd: function A(t) {
                var n = this.cpn;
                return f.checkPwd({ipt1: n.pwd, ipt2: n.pwd2, isSubmit: t})
            }, encrypt: function T(t) {
                var n = new window.NCJSEncrypt;
                return n.setPublicKey(a("#jsNCPublicKey").html() || ""), n.encrypt(a.trim(t))
            }
        })
    });