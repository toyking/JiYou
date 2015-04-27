using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace JiFriend.Models
{
    public class Job
    {
        [Key]
        public int id { get; set; }
        public string job { get; set; }
        public string company { get; set; }
        public string city { get; set; }
        public string salary { get; set; }
        public string email { get; set; }
        public string description { get; set; }
        public string requirement { get; set; }
        public string publisher { get; set; }
        public string publishtime { get; set; }
    }
}