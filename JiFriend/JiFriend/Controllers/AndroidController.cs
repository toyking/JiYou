using JiFriend.Models;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Web;
using System.Web.Mvc;

namespace JiFriend.Controllers
{
    public class AndroidController : Controller
    {
        

        // GET: /Android/
        public string Index()
        {
            return "Android";//SendMsg("18817325907", "您好，您的验证码为：1234，祝您交友顺利，旅途愉快！测试完毕~");
        }

        public static void log(String text)
        {
            StreamWriter sw = null;
            try
            {
                DirectoryInfo dir = new DirectoryInfo("F:/web/log/");
                if (!dir.Exists) dir.Create();
                String s = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss:fff") + " -> " + text;
                String fname = dir.ToString() + DateTime.Now.ToString("yyyy-MM-dd") + ".txt";
                Console.WriteLine(s);
                sw = new StreamWriter(fname, true);
                sw.WriteLine(s);
            }
            catch (Exception ex)
            {
                Console.Write("写日志[" + text + "]时遇到错误：" + ex);
            }
            finally
            {
                sw.Close();
            }
        }

        //Get: /Android/Login
        [HttpPost]
        public string Login()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("Login:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();
                string password = jsonobj["password"].ToString();

                User user = db.Users.FirstOrDefault(User => User.username == username && User.password == password);

                if (user != null)
                {
                    return "success";
                }
                else
                {
                    return "用户名或密码错误，登录失败";
                }
            }
            catch(Exception ex)
            {
                log("登录时遇到错误：" + ex);
                return "服务器出错";
            }
        }

        //Post: /Android/UserAdd
        [HttpPost]
        public string UserAdd()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("UserAdd:" + data);
                JsonSerializer serializer = new JsonSerializer();
                User user = (User)serializer.Deserialize(new JsonTextReader(new StringReader(data)), typeof(User));
                if (db.Users.FirstOrDefault(u => u.username == user.username) != null)
                {
                    return "注册失败，用户名已被注册";
                }
                db.Users.Add(user);
                db.SaveChanges();
                return "success";
            }
            catch (Exception ex)
            {
                log("注册时遇到错误：" + ex);
                return "服务器出错";
            }
        }

        //Post: /Android/GetUser
        [HttpPost]
        public string GetUser()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetUser:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();
                string password = jsonobj["password"].ToString();

                User user = db.Users.FirstOrDefault(User => User.username == username && User.password == password);

                if (user != null)
                {
                    return JsonConvert.SerializeObject(user);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取用户时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/EditUser
        [HttpPost]
        public string EditUser()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("EditUser:" + data);
                JsonSerializer serializer = new JsonSerializer();
                User user = (User)serializer.Deserialize(new JsonTextReader(new StringReader(data)), typeof(User));
                User user_from_db = db.Users.FirstOrDefault(u => u.id == user.id && u.username == user.username && u.password == user.password);
                if (user_from_db != null)
                {
                    JiFriendContext DB = new JiFriendContext();
                    DB.Entry(user).State = EntityState.Modified;
                    DB.SaveChanges();
                    return "success";
                }
                return "修改用户信息失败";
            }
            catch (Exception ex)
            {
                log("修改用户信息时遇到错误：" + ex);
                return "服务器出错";
            }
        }

        //Post: /Android/SquareAddStatus
        [HttpPost]
        public string SquareAddStatus()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("SquareAddStatus:" + data);
                JArray jarray = (JArray)JsonConvert.DeserializeObject(data);
                JsonSerializer serializer = new JsonSerializer();
                SquareStatus square_status = (SquareStatus)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[0].ToString())), typeof(SquareStatus));
                User user = (User)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[1].ToString())), typeof(User));
                User user_from_db = db.Users.FirstOrDefault(u => u.username == user.username && u.password == user.password);
                if (user_from_db != null && square_status.username == user.username)
                {
                    square_status.time = DateTime.Now.ToString("yyyy/MM/dd HH:mm");
                    JiFriendContext DB = new JiFriendContext();
                    DB.SquareStatuss.Add(square_status);
                    DB.SaveChanges();
                    return "success";
                }
                return "发布状态信息失败";
            }
            catch (Exception ex)
            {
                log("发布状态时遇到错误：" + ex);
                return "服务器出错";
            }
        }

       
        //Post: /Android/GetSquareStatusList
        [HttpPost]
        public string GetSquareStatusList()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetSquareStatusList:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();
                string password = jsonobj["password"].ToString();

                User user = db.Users.FirstOrDefault(User => User.username == username && User.password == password);

                if (user != null)
                {
                    List<SquareStatus> list = db.SquareStatuss.ToList();
                    return JsonConvert.SerializeObject(list);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取状态列表时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/PartyAdd
        [HttpPost]
        public string PartyAdd()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("PartyAdd:" + data);
                JArray jarray = (JArray)JsonConvert.DeserializeObject(data);
                JsonSerializer serializer = new JsonSerializer();
                Party party = (Party)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[0].ToString())), typeof(Party));
                User user = (User)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[1].ToString())), typeof(User));
                User user_from_db = db.Users.FirstOrDefault(u => u.username == user.username && u.password == user.password);
                if (user_from_db != null && party.username== user.username)
                {
                    party.publish_time = DateTime.Now.ToString("MM月dd日 HH:mm");
                    JiFriendContext DB = new JiFriendContext();
                    DB.Partys.Add(party);
                    DB.SaveChanges();
                    return "success";
                }
                return "发布活动信息失败";
            }
            catch (Exception ex)
            {
                log("发布活动时遇到错误：" + ex);
                return "服务器出错";
            }
        }

        //Post: /Android/GetPartyList
        [HttpPost]
        public string GetPartyList()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetPartyList:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();
                string password = jsonobj["password"].ToString();

                User user = db.Users.FirstOrDefault(u => u.username == username && u.password == password);

                if (user != null)
                {
                    List<Party> list = db.Partys.ToList();
                    return JsonConvert.SerializeObject(list);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取活动列表时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/GetPartyById
        [HttpPost]
        public string GetPartyById()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetPartyById:" + data);
                JObject jsonobj = JObject.Parse(data);
                int id = Convert.ToInt32(jsonobj["id"].ToString());

                Party party = db.Partys.FirstOrDefault(p => p.id == id);

                if (party != null)
                {
                    return JsonConvert.SerializeObject(party);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取活动详情时遇到错误：" + ex);
                return "fail";
            }
        }

        /*
        //Post: /Android/GetPartyPersonByUsername
        [HttpPost]
        public string GetPartyPersonByUsername()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetPartyPersonByUsername:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();

                User user = db.Users.FirstOrDefault(User => User.username == username);

                if (user != null)
                {
                    String ret = user.realname;
                    if (user.company != null && user.job != null) ret += " | " + user.company + user.job;
                    return ret;
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取用户名所对应的用户信息时遇到错误：" + ex);
                return "fail";
            }
        }*/

        //Post: /Android/GetPublicUserInfoByUsername
        [HttpPost]
        public string GetPublicUserInfoByUsername()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetPublicUserInfoByUsername:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();

                User user = db.Users.FirstOrDefault(u=> u.username == username);

                if (user != null)
                {
                    User user_public_info = new User();
                    user_public_info.city = user.city;
                    user_public_info.realname = user.realname;
                    user_public_info.username = user.username;
                    user_public_info.company = user.company;
                    user_public_info.job = user.job;
                    return JsonConvert.SerializeObject(user_public_info);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取用户名所对应的用户信息时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/SquarePrizeAdd
        [HttpPost]
        public string SquarePrizeAdd()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("SquarePrizeAdd:" + data);
                JObject jsonobj = JObject.Parse(data);
                int square_id = Convert.ToInt32(jsonobj["square_id"].ToString());
                string username = jsonobj["username"].ToString();
                string password = jsonobj["password"].ToString();

                if ( db.Users.FirstOrDefault(u=> u.username==username && u.password==password)!=null)
                {
                    if (db.SquarePrizes.FirstOrDefault(sp => sp.username == username && sp.square_id == square_id) == null)
                    {
                        SquarePrize square_prize = new SquarePrize();
                        square_prize.square_id = square_id;
                        square_prize.username = username;
                        square_prize.time = DateTime.Now.ToString("yyyy/MM/dd HH:mm");
                        db.SquarePrizes.Add(square_prize);
                        db.SaveChanges();
                        return "success";
                    }
                    else
                    {
                        return "已经点过赞了，orz ..";
                    }
                }
                else
                {
                    return "点赞失败，用户不存在！";
                }
            }
            catch (Exception ex)
            {
                log("点赞时遇到错误：" + ex);
                return "点赞失败";
            }
        }

        //Post: /Android/GetSquarePrize
        [HttpPost]
        public string GetSquarePrize()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetSquarePrize:" + data);
                JObject jsonobj = JObject.Parse(data);
                int square_id = Convert.ToInt32(jsonobj["square_id"].ToString());
                List<SquarePrize> list_square_prize = db.SquarePrizes.Where(sp => sp.square_id == square_id).ToList();
                return JsonConvert.SerializeObject(list_square_prize);
            }
            catch (Exception ex)
            {
                log("获取赞时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/GetPartyAttenderList
        [HttpPost]
        public string GetPartyAttenderList()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetPartyAttenderList:" + data);
                JObject jsonobj = JObject.Parse(data);
                int party_id = Convert.ToInt32(jsonobj["party_id"].ToString());
                List<PartyAttender> list_party_attender = db.PartyAttenders.Where(pa => pa.party_id == pa.party_id).ToList();
                return JsonConvert.SerializeObject(list_party_attender);
            }
            catch (Exception ex)
            {
                log("获取报名用户时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/PartyAttenderAdd
        [HttpPost]
        public string PartyAttenderAdd()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("PartyAttenderAdd:" + data); ;
                JArray jarray = (JArray)JsonConvert.DeserializeObject(data);
                JsonSerializer serializer = new JsonSerializer();
                PartyAttender party_attender = (PartyAttender)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[0].ToString())), typeof(PartyAttender));
                User user = (User)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[1].ToString())), typeof(User));

                if (db.Users.FirstOrDefault(u => u.username == user.username && u.password == user.password) != null && user.username == party_attender.username)
                {
                    if (db.PartyAttenders.FirstOrDefault(pa => pa.username == user.username && pa.party_id == party_attender.party_id ) == null)
                    {
                        party_attender.time = DateTime.Now.ToString("yyyy/MM/dd HH:mm");
                        db.PartyAttenders.Add(party_attender);
                        db.SaveChanges();
                        return "success";
                    }
                    else
                    {
                        return "已经报名活动了，orz ..";
                    }
                }
                else
                {
                    return "报名活动失败！";
                }
            }
            catch (Exception ex)
            {
                log("报名活动时遇到错误：" + ex);
                return "报名活动失败";
            }
        }

        //Post: /Android/JobAdd
        [HttpPost]
        public string JobAdd()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("JobAdd:" + data);
                JArray jarray = (JArray)JsonConvert.DeserializeObject(data);
                JsonSerializer serializer = new JsonSerializer();
                Job job = (Job)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[0].ToString())), typeof(Job));
                User user = (User)serializer.Deserialize(new JsonTextReader(new StringReader(jarray[1].ToString())), typeof(User));
                User user_from_db = db.Users.FirstOrDefault(u => u.username == user.username && u.password == user.password);
                if (user_from_db != null && job.publisher == user.username)
                {
                    job.publishtime = DateTime.Now.ToString("yyyy/MM/dd HH:mm");
                    JiFriendContext DB = new JiFriendContext();
                    DB.Jobs.Add(job);
                    DB.SaveChanges();
                    return "success";
                }
                return "发布实习信息失败";
            }
            catch (Exception ex)
            {
                log("发布实习时遇到错误：" + ex);
                return "服务器出错";
            }
        }

        //Post: /Android/GetPartyList
        [HttpPost]
        public string GetJobList()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetJobList:" + data);
                JObject jsonobj = JObject.Parse(data);
                string username = jsonobj["username"].ToString();
                string password = jsonobj["password"].ToString();

                User user = db.Users.FirstOrDefault(u => u.username == username && u.password == password);

                if (user != null)
                {
                    List<Job> list = db.Jobs.ToList();
                    return JsonConvert.SerializeObject(list);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取实习列表时遇到错误：" + ex);
                return "fail";
            }
        }

        //Post: /Android/GetPartyById
        [HttpPost]
        public string GetJobById()
        {
            try
            {
                JiFriendContext db = new JiFriendContext();
                string data = Request.Params["data"];
                log("GetJobById:" + data);
                JObject jsonobj = JObject.Parse(data);
                int job_id = Convert.ToInt32(jsonobj["job_id"].ToString());

                Job job = db.Jobs.FirstOrDefault(j => j.id == job_id);

                if (job != null)
                {
                    return JsonConvert.SerializeObject(job);
                }
                else
                {
                    return "fail";
                }
            }
            catch (Exception ex)
            {
                log("获取实习详情时遇到错误：" + ex);
                return "fail";
            }
        }


        /*
        /// <summary>
        /// 以 HTTP 的 POST 提交方式 发送短信(ASP.NET的网页或是C#的窗体，均可使用该方法)
        /// </summary>
        /// <param name="mobile">要发送的手机号码</param>
        /// <param name="msg">要发送的内容</param>
        /// <returns>发送结果</returns>
        public string SendMsg(string mobile, string msg)
        {
            string name = "18817325907";
            string pwd = "400EE3FE5FC2BE3CF0CE4AE13477";//登陆web平台 http://web.cr6868.com  在管理中心--基本资料--接口密码（28位） 如登陆密码修改，接口密码会发生改变，请及时修改程序
            string sign = "计友";             //一般为企业简称
            StringBuilder arge = new StringBuilder();

            arge.AppendFormat("name={0}", name);
            arge.AppendFormat("&pwd={0}", pwd);
            arge.AppendFormat("&content={0}", msg);
            arge.AppendFormat("&mobile={0}", mobile);
            arge.AppendFormat("&sign={0}", sign);
            arge.Append("&type=pt");
            string weburl = "http://web.cr6868.com/asmx/smsservice.aspx";

            string resp = PushToWeb(weburl, arge.ToString(), Encoding.UTF8);
            log(resp);
            if (resp.Split(',')[0] == "0")
            {
                return "success";//提交成功
            }
            else
            {
                return "fail";//提交失败，可能余额不足，或者敏感词汇等等
            }

            //return resp;//是一串 以逗号隔开的字符串。阅读文档查看响应的意思
        }

        /// <summary>
        /// HTTP POST方式
        /// </summary>
        /// <param name="weburl">POST到的网址</param>
        /// <param name="data">POST的参数及参数值</param>
        /// <param name="encode">编码方式</param>
        /// <returns></returns>
        public string PushToWeb(string weburl, string data, Encoding encode)
        {
            byte[] byteArray = encode.GetBytes(data);

            HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(new Uri(weburl));
            webRequest.Method = "POST";
            webRequest.ContentType = "application/x-www-form-urlencoded";
            webRequest.ContentLength = byteArray.Length;
            Stream newStream = webRequest.GetRequestStream();
            newStream.Write(byteArray, 0, byteArray.Length);
            newStream.Close();

            //接收返回信息：
            HttpWebResponse response = (HttpWebResponse)webRequest.GetResponse();
            StreamReader aspx = new StreamReader(response.GetResponseStream(), encode);
            return aspx.ReadToEnd();
        }*/

        /*
        public static void ConnectSSL()
        {

            WebRequest request = WebRequest.Create("https://api.sms.mob.com/sms/verify");
            request.Proxy = null;
            request.Credentials = CredentialCache.DefaultCredentials;

            //allows for validation of SSL certificates 

            ServicePointManager.ServerCertificateValidationCallback += new System.Net.Security.RemoteCertificateValidationCallback(ValidateServerCertificate);
            byte[] bs = Encoding.UTF8.GetBytes("appkey=60d21cbc7b5a&phone=18817325907&zone=86&code=432432ds");
            request.Method = "Post";
            using (Stream reqStream = request.GetRequestStream())
            {
                reqStream.Write(bs, 0, bs.Length);
            } 
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            Stream dataStream = response.GetResponseStream();
            StreamReader reader = new StreamReader(dataStream);
            string responseFromServer = reader.ReadToEnd();
            log(responseFromServer);
        }

        //for testing purpose only, accept any dodgy certificate... 
        public static bool ValidateServerCertificate(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors sslPolicyErrors)
        {
            log(certificate.ToString());
            log(chain.ToString());
            log(sslPolicyErrors.ToString());
            return true;
        }*/

    }
}