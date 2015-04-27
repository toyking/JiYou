using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using JiFriend.Models;

namespace JiFriend.Controllers
{
    public class PartyAttendersController : Controller
    {
        private JiFriendContext db = new JiFriendContext();

        // GET: PartyAttenders
        public ActionResult Index()
        {
            return View(db.PartyAttenders.ToList());
        }

        // GET: PartyAttenders/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            PartyAttender partyAttender = db.PartyAttenders.Find(id);
            if (partyAttender == null)
            {
                return HttpNotFound();
            }
            return View(partyAttender);
        }

        // GET: PartyAttenders/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: PartyAttenders/Create
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "id,party_id,username,reason,time,ischeked")] PartyAttender partyAttender)
        {
            if (ModelState.IsValid)
            {
                db.PartyAttenders.Add(partyAttender);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(partyAttender);
        }

        // GET: PartyAttenders/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            PartyAttender partyAttender = db.PartyAttenders.Find(id);
            if (partyAttender == null)
            {
                return HttpNotFound();
            }
            return View(partyAttender);
        }

        // POST: PartyAttenders/Edit/5
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "id,party_id,username,reason,time,ischeked")] PartyAttender partyAttender)
        {
            if (ModelState.IsValid)
            {
                db.Entry(partyAttender).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(partyAttender);
        }

        // GET: PartyAttenders/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            PartyAttender partyAttender = db.PartyAttenders.Find(id);
            if (partyAttender == null)
            {
                return HttpNotFound();
            }
            return View(partyAttender);
        }

        // POST: PartyAttenders/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            PartyAttender partyAttender = db.PartyAttenders.Find(id);
            db.PartyAttenders.Remove(partyAttender);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
