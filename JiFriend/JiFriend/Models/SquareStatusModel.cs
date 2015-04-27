using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace JiFriend.Models
{
    public class SquareStatus
    {
        [Key]
        public int id { get; set; }
        public string time { get; set; }
        public string content { get; set; }
        public string username { get; set; }
    }
}