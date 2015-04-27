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
    public class SquareStatusController : Controller
    {
        private JiFriendContext db = new JiFriendContext();

        // GET: SquareStatus
        public ActionResult Index()
        {
            return View(db.SquareStatuss.ToList());
        }

        // GET: SquareStatus/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            SquareStatus squareStatus = db.SquareStatuss.Find(id);
            if (squareStatus == null)
            {
                return HttpNotFound();
            }
            return View(squareStatus);
        }

        // GET: SquareStatus/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: SquareStatus/Create
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "id,time,content,username")] SquareStatus squareStatus)
        {
            if (ModelState.IsValid)
            {
                db.SquareStatuss.Add(squareStatus);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(squareStatus);
        }

        // GET: SquareStatus/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            SquareStatus squareStatus = db.SquareStatuss.Find(id);
            if (squareStatus == null)
            {
                return HttpNotFound();
            }
            return View(squareStatus);
        }

        // POST: SquareStatus/Edit/5
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "id,time,content,username")] SquareStatus squareStatus)
        {
            if (ModelState.IsValid)
            {
                db.Entry(squareStatus).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(squareStatus);
        }

        // GET: SquareStatus/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            SquareStatus squareStatus = db.SquareStatuss.Find(id);
            if (squareStatus == null)
            {
                return HttpNotFound();
            }
            return View(squareStatus);
        }

        // POST: SquareStatus/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            SquareStatus squareStatus = db.SquareStatuss.Find(id);
            db.SquareStatuss.Remove(squareStatus);
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
