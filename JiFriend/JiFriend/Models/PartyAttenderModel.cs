using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace JiFriend.Models
{
    public class PartyAttender
    {
        [Key]
        public int id { get; set; }
        public int party_id { get; set; }
        public String username { get; set; }
        public String reason { get; set; }
        public String time { get; set; }
        public bool ischeked { get; set; }
    }
}