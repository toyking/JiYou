using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace JiFriend.Models
{
    public class Party
    {
        [Key]
        public int id { get; set; }
        public string theme { get; set; }
        public string time { get; set; }
        public string address { get; set; }
        public int peoplenum { get; set; }
        public string description { get; set; }
        public string publish_time { get; set; }
        public string username { get; set; }
    }
}