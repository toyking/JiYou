using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace JiFriend.Models
{
    public class User
    {
        [Key]
        public int id { get; set; }
        public string username { get; set; }
        public string password { get; set; }
        public string realname { get; set; }
        public string phone { get; set; }
        public string sex { get; set; }
        public string company { get; set; }
        public string job { get; set; }
        public string city { get; set; }
        public string address { get; set; }
        public string email { get; set; }
        public string qq { get; set; }
        public string studentid { get; set; }
    }
    
}